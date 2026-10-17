# Modern app revision

Replaces the ornamental courtyard theme with a borderless carbon/slate interface, electric-lime actions, periwinkle accents, rounded native surfaces and clean sans-serif typography. No baked-in UI PNG is needed: text, layout, selection, accessibility and touch feedback remain native Compose. The prior artwork remains in repository history/assets but is no longer used for screen backgrounds or team badges.

English/Nepali selection is available on the welcome screen and setup, with the existing persisted application language used throughout. Film titles and online poster fetching continue through the existing data sources/cache. The main movie card now uses a full-bleed poster with a legibility scrim and native title/metadata, plus a small hide/reveal control. Missing posters show an intentional icon fallback.

Correct, pass and time-up now advance directly to the next team's handoff. The previous result is displayed on that same screen. One tap on **Reveal & start** starts the round and reveals the movie to the actor. This removes separate result confirmation and reveal taps while retaining the handoff needed to keep the next film secret.

The visible activity sets FLAG_KEEP_SCREEN_ON in onResume and clears it in onPause; this covers menu/setup/dialog/gameplay while allowing normal display sleep after leaving the app. It does not override the power button or user locking the display.

At ten seconds remaining (or the first observed value below ten), the service plays a distinct ascending three-tone warning, followed by the remaining per-second ticks. The warning is consumed once per round and does not repeat on pause/resume. Mute, audio focus, silent/vibrate mode and system volume remain respected.

Validation includes existing game/recovery/Compose coverage plus direct-next-team/reveal and once-per-round warning tests. Physical-device display timeout, audio audibility/routing and lock-screen checks remain necessary.

## Approved artwork and interaction pass

The approved Nepali charades artwork is bundled as a 1000px WebP (no network request for the home hero). Its responsive wide composition keeps both actors visible; all headings, language switching and controls remain native. The matching clapperboard/play/Dhaka-topi artwork is used for legacy and adaptive launchers, with a separate monochrome vector for themed icons.

Interaction animation is bounded: 3% button press compression with spring release, a small selected-category scale change, a gang-badge fade/pop when its name changes, animated score changes, and a 360ms home entrance. Compose's animation duration scale is respected. No idle loops were added outside gameplay. The existing gameplay clock remains lifecycle-aware and stops when paused.

Poster discovery now requests a 1024px MediaWiki thumbnail with original fallback before trying the existing summary/image-info fallbacks. Summary parsing prefers its original source and ignores empty/invalid source strings. The original image may still be small. Coil retains its cache and card-bound decoding; a dim crop fills the backdrop while the main poster is fitted without stretching or cropping. The enclosing card retains its 28dp corner radius and native readable title overlay. API response fixtures cover source precedence and missing URLs without live requests.
