# Nebular Recorder

This is the desktop component of the Nebular Recorder project.

## Development

The project uses Java 16 and JavaFX

- Clone repository
- `gradle run` 
- Profit

### Linux

Make sure to always pass the following jvm args  to avoid GTK 3 issues: `-Djdk.gtk.version=2 -Dprism.verbose=true` (they are already defined in the build.gradle but keep it in mind for your IDE setup)

