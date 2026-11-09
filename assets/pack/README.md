# Default identity pack (`embedded-default`)

**365** programs · **32** conferences · **2026–27** membership.

Regenerate after `planning/league-branding.md` membership changes:

```bash
python3 planning/tools/generate_default_pack.py
```

Output copies to:

- `assets/pack/embedded-default.json` (source of truth in repo)
- `android/app/src/main/assets/pack/embedded-default.json` (shipped in APK)

Validated by [`IdentityPackCodec`](../../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/pack/IdentityPackCodec.kt) at build/CI time.

TXT / URL import grammar and template: [`identity-import-template.txt`](identity-import-template.txt). Runtime pipeline: [`IdentityPackImportPipeline`](../../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/pack/import/IdentityPackImportPipeline.kt).
