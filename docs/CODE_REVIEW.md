# LeapCarPlay code review

Scope: application identity and release metadata, video queueing and MediaCodec output, external-display selection/lifecycle, and connection/controller shutdown paths. This review separates source-level findings from behavior that still requires an 8155 vehicle.

## Findings and changes

### P1 — Video queue accounting scaled with the backlog on every frame — fixed

`VideoDecodeQueue.offer()` previously filtered the whole blocking queue for frames and summed every queued payload before deciding whether to drop. That work ran on the producer path for each video frame, so the cost increased as latency accumulated. The queue now maintains frame count and byte totals under one lock and updates them as frames enter, leave, or are discarded. Overflow still clears dependent frames and emits a resync marker; an individually oversized frame is rejected. Boundary and polling coverage is in `VideoDecodeQueueTest`.

### P1 — Fresh installs negotiated 30 fps despite a 60 fps setting — fixed

The display setting already allowed 60 fps, but persistence supplied a literal 30 fps when the preference was absent. New installs now use `AirPlayDisplaySettings.DEFAULT_FPS` (60); 30 fps remains selectable. Persistence and settings tests cover the default and lower bound. This is a configuration default, not evidence that an 8155 sustains 60 fps.

### P2 — External-display routing had implicit selection and lifecycle state — improved

`CarPlayHostActivity.refreshClusterDisplay()` previously embedded HDMI2 name matching and single-display fallback in the Activity, while surface attachment and display-listener registration were managed separately. Display selection is now an independently tested policy: prefer an HDMI2-named presentation display; use an unnamed display only when it is the sole candidate. Surface replacement guarantees detach-before-attach and ignores duplicate callbacks, and listener unregistration is idempotent. HDMI2 remains output-only; touch input is still handled only by the main display. Tests cover selection ambiguity, surface replacement and clearing.

### P2 — Package identity and documentation mixed upstream and fork identities — fixed where user-facing

The mobile and Automotive APK application IDs are now `com.signxer.leapcarplay` and `com.signxer.leapcarplay.automotive`; the debug demo suffix, P2P ownership prefix expectations, BYD debug bridge allowlist, app-private preference/channel names, stop action and diagnostic filename were updated. The Kotlin namespace and iAP2 protocol identifier intentionally remain upstream values because they are code/protocol namespaces rather than Android install identity. Android treats the new IDs as separate apps, so old preferences and pairings are not migrated. The site now points to the fork and no longer links its update button to the upstream BYD Telegram channel.

### P2 — Existing compatibility claims could be read as 8155 verification — clarified

Previous physical validation in the inherited compatibility document was performed on a BYD development car. The document now says that does not verify a Leapmotor 8155, distinguishes BYD-only HUD/hotspot behavior, and states that HDMI2 map content depends on the stream supplied by the iPhone. No source inspection can establish whether a chosen navigation app supplies a cluster map in the target vehicle.

## Follow-up recommendations

1. **P1, vehicle validation:** measure rendered frame rate and decoder errors with main display plus 720×720 HDMI2 active at 60 fps, including sustained navigation and reconnect. Record thermal behavior, then repeat at 30 fps. Do not infer hardware throughput from Snapdragon 8155 specifications alone.
2. **P1, vehicle validation:** unplug/reconnect HDMI2 during an active session and verify main-screen continuity, cluster stream recovery, audio and clean reconnection. Confirm a navigation app actually sends a map to the alternate stream.
3. **P2, maintainability:** `CarPlayHostActivity` and `CarPlayController` remain very large classes. This change extracts and tests display selection and surface ownership, but a broader controller/session split needs separate seams and regression coverage before it is safe to undertake.
4. **P2, build coverage:** CI now builds both APK variants and runs the existing unit tests and lint. The local checkout used for this review had no Android SDK installed, so Gradle could not configure Android projects locally; CI status must be checked after upload.

## Validation limits

Source checks can verify package strings, routing policy, queue bounds and lifecycle ownership. They cannot verify decoder throughput, panel timing, thermal throttling, HDMI hot-plug behavior, vehicle firmware restrictions, or whether a navigation app supplies cluster content. No claim about those outcomes is made here.
