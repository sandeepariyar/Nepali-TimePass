# Approved home and timer layout

Continue development on `feature/modern-slick-ui` (PR #2), not the already merged
`feature/nepali-cinematic-ui` branch. Keep PR #2 unmerged for review.

The modern branch already contains the approved rooftop artwork, app-icon artwork,
larger Wikipedia poster requests, proportional poster rendering, button spring
feedback and a finite home entrance animation.

This follow-up adds working cinema presets and bilingual help to the home screen.
The selection feeds the existing settings and survives entering game setup.
The home headline and artwork occupy separate areas so text cannot cover faces
on small phones; the page scrolls for short displays and large text.

Gameplay places pause/resume beside the countdown and gives pass/skip two wider
buttons. A progress strip below the timer changes to coral in the last ten seconds.
The large rounded poster card, hide/reveal privacy, scoring, background timer,
mute, lifecycle-safe effects and one-tap team handoff remain intact.

No merge is performed. Physical-device poster networking and locked-screen audio
still need the device checks listed in MODERN_UI.md.
