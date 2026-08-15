#!/usr/bin/env bash
#
# new-case-branch.sh — Lag en ny case-forsøk-branch fra main.
#
# Følger treningsprotokollen i docs/TRENINGSGUIDE.md, punkt 2:
# git switch -c case-NN-forsoek-M main
#
# Skriptet bruker "git switch -C" (force-create) i stedet for "-c".
# Det betyr at hvis branchen allerede finnes, blir den resatt til main
# i stedet for at skriptet feiler — praktisk hvis du vil starte et
# forsøk helt på nytt uten å slette branchen manuelt først.
#
# Bruk:
# ./new-case-branch.sh <case-nummer> <forsoek-nummer>
#
# Eksempel:
# ./new-case-branch.sh 5 2
# -> oppretter/resetter branch case-05-forsoek-2 fra main
set -euo pipefail
shopt -s nullglob

usage() {
 echo "Bruk: $0 <case-nummer> <forsoek-nummer>" >&2
 echo "Eksempel: $0 5 2 (oppretter case-05-forsoek-2 fra main)" >&2
}

if [[ $# -ne 2 ]]; then
 usage
 exit 1
fi

case_arg="$1"
attempt_arg="$2"

if ! [[ "$case_arg" =~ ^[0-9]+$ ]]; then
 echo "Feil: case-nummer må være et heltall, fikk '$case_arg'." >&2
 exit 1
fi

if ! [[ "$attempt_arg" =~ ^[0-9]+$ ]]; then
 echo "Feil: forsøk-nummer må være et heltall, fikk '$attempt_arg'." >&2
 exit 1
fi

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

if ! git -C "$script_dir" rev-parse --is-inside-work-tree >/dev/null 2>&1; then
 echo "Feil: $script_dir er ikke et git-repo." >&2
 exit 1
fi

repo_root="$(git -C "$script_dir" rev-parse --show-toplevel)"
cd "$repo_root"

# Normaliser case-nummer til to siffer (10# unngår oktal-tolkning av f.eks. "08").
case_num=$(printf "%02d" "$((10#$case_arg))")
attempt_num=$((10#$attempt_arg))

matches=(case-"${case_num}"-*/)
if [[ ${#matches[@]} -eq 0 ]]; then
 echo "Feil: fant ingen modul som matcher 'case-${case_num}-*' i $repo_root." >&2
 echo "Tilgjengelige caser:" >&2
 for d in case-[0-9][0-9]-*/; do
 echo " - ${d%/}" >&2
 done
 exit 1
fi

case_dir="${matches[0]%/}"
branch="case-${case_num}-forsoek-${attempt_num}"

echo "Case-modul: $case_dir"
echo "Branch: $branch (fra main)"
echo

git switch -C "$branch" main

cat <<EOF

Branch '$branch' er opprettet fra main.

Neste steg (docs/TRENINGSGUIDE.md):
 1. Les $case_dir/README.md grundig.
 2. Sett tidsboks etter casets "## Tid".
 3. Kjør: mvn test -pl $case_dir
 4. Skriv kontrakttester først der det er naturlig (TDD).
 5. Debrief høyt til slutt, og registrer score i STATUS.md på main.
EOF
