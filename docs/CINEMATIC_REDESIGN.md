# Nepali TimePass cinematic redesign

Branch: `feature/nepali-cinematic-ui`; base: `c25f20b98e3692288912275088eecdebb63472d4`. Main is not merged or replaced.

## Implementation

- Kotlin and Compose retained. Existing Wikipedia film discovery, online poster providers, movie catalogues, custom-film model, 32-pair Nepali gang generator, category selection and scoring remain in place.
- Welcome/menu, setup/category selection, editable gang names and colours, actor handoff, active round, correct/pass/time-up summary, winners, settings and bilingual help share the cinema courtyard design system. Skip transitions change only the movie; pass ends the turn. Movie titles, gang names, countdown and controls are native UI.
- Kathmandu/Newari-inspired dusk courtyard illustration, carved timber, brass, marigolds and vermilion/indigo palette. Two transparent illustrated gang crests (lion and red panda with madal) have native team-colour surrounds. Team identity follows team slot; generated gang names remain freely editable.
- Timer progress is a row of extinguishing marquee bulbs. Final ten seconds warm the light and introduce slow embers. It uses inexpensive Canvas drawing, with no runtime shader or GPU feature requirement. One frame clock runs only on the gameplay screen while resumed and unpaused. Other animations are finite transitions; winner confetti ends after 1.4 seconds.
- Gameplay uses fixed countdown/scoring controls and a scrolling movie area. Landscape separates the film from timer/controls. Other screens scroll; larger displays cap content width. Platform text fallback supports Devanagari without rasterized text or Latin letter spacing.
- Poster loads retain the existing providers and URLs, add per-film request coalescing and a ten-minute negative-cache window, and remember Coil requests. Startup prefetch is bounded instead of walking the whole database. Coil is initialized once per process with a 15% memory cache and 50 MB disk cache.
- Game/settings/custom films are saved locally. Same-boot interrupted sessions restore a monotonic deadline or paused subsecond remainder. After reboot an interrupted round is replayed without awarding a result. The deck is reconstructed from the selected filters on refill; exact shuffled order is not persisted.

## Android countdown and sound

`DeadlineClock` uses `SystemClock.elapsedRealtime`; delayed callbacks catch up to an absolute deadline. Pause keeps milliseconds, so repeated pause/resume cannot add rounded seconds. Correct/pass/next-turn callbacks are phase guarded, and actions at an expired deadline cannot award points.

A user-started `specialUse` foreground service owns deadline monitoring, the lock-screen notification/chronometer, final percussion and time-up cue. A partial wake lock is limited to the remaining 30–600-second round plus four seconds and is released on pause/result/exit/destruction. The service is not sticky and does not launch from boot. No exact alarm, full-screen intent, overlay, storage permission, battery exemption or volume override is requested.

Android 13 notification permission is requested at the first round only. Denial does not block play; the UI explains the hidden lock-screen countdown. Notification content intentionally excludes the secret film. Pause is available from the notification; resume happens inside the app. OS notification privacy and channel visibility settings still apply.

Sound uses one serialized low-amplitude synthesized voice with short attack/decay envelopes and madal-inspired pitched percussion. Each AudioTrack is released in `finally`; scopes are cancelled on cleanup. Transient audio focus is required and loss stops the cue. Mute is available in setup and during a round; silent/vibrate mode is respected, with no device-volume changes. Service time-up audio is emitted once, rather than duplicated by the ViewModel.

Official Android references:
- https://developer.android.com/develop/background-work/services/fgs/service-types#special-use
- https://developer.android.com/develop/background-work/services/fgs/restrictions-bg-start
- https://developer.android.com/develop/ui/views/notifications/notification-permission
- https://developer.android.com/media/optimize/audio-focus

`specialUse` must be declared and reviewed in Play Console for distribution. This implementation does not assume a Play policy exemption or misuse media playback/data-sync service types.

## Art assets and provenance

Generated using the built-in imagegen tool, then resized/encoded as WebP with alpha preserved. No third-party character/IP assets are introduced. Runtime assets live in `app/src/main/res/drawable-nodpi/`:

| Asset | Purpose | Resolution |
|---|---|---|
| cinema_courtyard.webp | Shared static full-bleed background, quiet centre for native UI | 864 × 1296 |
| gang_lion.webp | Transparent carved timber/brass lion cinema crest | 382 × 408 |
| gang_panda.webp | Transparent red panda/madal crest | 403 × 384 |

Generation prompts: premium hand-painted Kathmandu/Newari cinema courtyard at festival dusk, carved timber, small brass marquee bulbs, marigolds, indigo and burgundy shadows, quiet dark central negative space, two small contemporary Nepali friends at bottom corners; no text or UI. Separate transparent crest prompts specified a brass lion with cinema sunglasses/film reel and a Himalayan red panda with madal, both in Newari-inspired carved timber shields, marigolds, no text, generous transparent margin. The original generated PNGs were shown in the task conversation; optimized runtime files are committed.

The adaptive launcher now uses the lion crest on an indigo background. The existing legacy launcher WebPs contained UTF-8-encoded binary bytes in the base repository. They were decoded back to valid image bytes without changing their visual design. Original legacy background assets are retained on the branch.

## Verification and manual acceptance

Automated coverage: pure deadline tests (delayed callbacks, subsecond pause, repeated pause/resume, expiry, stop), Robolectric game tests (duplicate scoring, invalid phases, pass/next turn, duration bounds), Compose hidden-film/reveal and pause-control tests, and a small-phone Devanagari screenshot. Existing catalogue/gang tests remain. `.github/workflows/android.yml` builds APK, runs unit/UI-on-Robolectric tests and lint, and uploads the APK/reports.

Before device release, verify on Android 13, 14, 15 and 16:

1. Grant and deny notifications; check lock-screen privacy, countdown, tap-to-open and notification pause. No secret movie should appear in the notification.
2. Start 30/180/600-second rounds; Home and lock the display. Check time-up within a second, sound once, notification/service removal, no lingering wake lock. Inspect `adb shell dumpsys activity services` and `adb shell dumpsys power` after completion.
3. Pause at a fractional second, wait while locked, resume; elapsed paused time must not count. Try rapid pause/resume/correct/pass/skip near expiry.
4. Rotate during gameplay and after results; scores, revealed state and deadline remain stable. Use 320dp-width phones, landscape, 200% font scale, TalkBack and Nepali/English text. Check conjuncts such as काठमाडौँ, चलचित्र, ग्याङ, अङ्क.
5. Repeat matches, use custom films and online search, switch category mixes, disconnect/reconnect networking, and confirm poster cache reuse. Check fresh-install offline fallback catalogue.
6. Exercise mute, silent/vibrate/DND, Bluetooth disconnect and competing media/audio focus. No cue may change device volume or bypass policy.
7. Remove the task while a round runs, reopen before and after expiry, kill the process, and reboot. Persisted state should restore/replay appropriately without duplicate points. Exact deck order is intentionally not retained.

Limits: force-stop, Android's foreground-service Task Manager stop, reboot, OEM process termination and explicit device restrictions can prevent background audio. No exact alarm is used to defeat these decisions. The app recovers elapsed state when opened, but cannot promise a sound after forced termination. Physical-device lock-screen/audio-focus/OEM behavior and Play approval cannot be established by a JVM test or debug build.

## Recorded UI previews

These are captures of the running Compose UI under Robolectric native graphics, rather than image mockups: [welcome](previews/welcome.webp), [setup](previews/setup.webp), [gameplay in Nepali](previews/nepali-small.webp), [landscape](previews/landscape.webp), [handoff](previews/ready.webp), [winner](previews/winner.webp).

## Local validation result — 15 October 2026

- Gradle 9.3.1, JDK 17, Android platform 36.1; debug APK assembled successfully at `app/build/outputs/apk/debug/app-debug.apk` (about 23 MB).
- 27 JVM/Robolectric/Compose tests passed, including deadline recovery and persistence, duplicate interactions, pause controls, secret-film reveal, setup/categories, landscape and Devanagari rendering.
- `lintDebug` passed with zero errors. Eighteen advisory warnings concern retained unused resources, dependency update suggestions, KTX conveniences and a redundant label; no lint baseline or disabled error checks were used.
- Recorded screenshots were visually reviewed. Phone layout captures use 360 × 640 dp; landscape uses 800 × 360 dp. These do not replace physical-device rotation, lock-screen, audio and accessibility checks.
- Validation command: `./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug -Proborazzi.test.record=true`. Local validation used the same Gradle distribution directly while preparing the missing wrapper. Dependencies were downloaded online, then the final check also ran with the cache in offline mode.
- The first CI attempt failed in SDK setup because an action default requested the removed `tools` package. The workflow now specifies the supported command-line tools and `platform-tools`, and coalesces duplicate push/PR runs by branch.
