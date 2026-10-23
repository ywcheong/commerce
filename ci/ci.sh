#!/usr/bin/env bash
# Usage: ci.sh pre-commit | commit-msg <msg-file> | all
set -euo pipefail
cd "$(dirname "$0")/.."

fail() { echo "::error::$*" >&2; exit 1; }

gate() {
  local name=$1 start=${EPOCHREALTIME/[.,]/} rc=0 us elapsed
  shift
  echo "::group::$name"
  ( "$@" ) || rc=$?
  echo "::endgroup::"
  us=$(( ${EPOCHREALTIME/[.,]/} - start ))
  elapsed=$(printf '%d.%03ds' $((us / 1000000)) $((us / 1000 % 1000)))
  if (( rc == 0 )); then
    echo "$name: ok ($elapsed)"
  else
    echo "::error title=$name::failed ($elapsed)"
  fi
  return $rc
}

check_tdd_order() {
  local prev=$1 cur=$2
  [[ $cur == feat:* ]] || return 0
  [[ $prev == "test:${cur#feat:}" ]] || fail "'$cur' 직전에 'test:${cur#feat:}' 커밋이 필요하다"
}

format_staged() {
  local files
  mapfile -t files < <(git diff --cached --name-only --diff-filter=ACMR -- '*.kt' '*.kts')
  (( ${#files[@]} )) || return 0
  ./gradlew ktlintFormat
  git add -- "${files[@]}"
}

check_whitespace() {
  git diff --cached --check || fail "공백 오류"
}

check_last_tdd_order() {
  local prev
  prev=$(git log -1 --format=%s 2>/dev/null || true)
  check_tdd_order "$prev" "$(head -n1 "$1")"
}

check_history_tdd_order() {
  local prev="" cur
  while IFS= read -r cur; do
    check_tdd_order "$prev" "$cur"
    prev=$cur
  done < <(git log --reverse --format=%s)
}

case "${1:-}" in
  pre-commit)
    gate format format_staged
    gate whitespace check_whitespace
    gate gradle ./gradlew check
    ;;
  commit-msg)
    gate tdd-order check_last_tdd_order "${2:?메시지 파일 필요}"
    ;;
  all)
    gate tdd-order check_history_tdd_order
    gate gradle ./gradlew check
    ;;
  *) fail "Usage: ci.sh pre-commit | commit-msg <msg-file> | all" ;;
esac
