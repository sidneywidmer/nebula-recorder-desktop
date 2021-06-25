# Nebular Recorder

This is the desktop component of the Nebular Recorder project.

## Development

The project uses Java 16 and JavaFX

- Clone repository
- Install ffmpeg (best done with choco https://community.chocolatey.org/packages/ffmpeg)
- Copy the `main/resources/app.example.conf` to `main/resources/app.conf`
  - set the `endpoint` either to your local server or to `https://nebula.sidney.dev` 
  - set the `storage` to some writable directory, eg `C:\\tmp`
  - set the `implementation` to `ffmpeg` 
- `gradle run`
- You should then see the GUI where you can login, if you don't have an account create one on the server you provided as `endpoint`

### Linux

Make sure to always pass the following jvm args  to avoid GTK 3 issues: `-Djdk.gtk.version=2 -Dprism.verbose=true` (they are already defined in the build.gradle but keep it in mind for your IDE setup)

## Documentation

### General concept

We're using JavaFx fxml files that have a controller attached to render the different views: login, main, selectArea. When javaFx resolves a controller, it automatically does the dependency injection for us thanks to Googles Guice library we're also using on the server. For config management we're also using lightbend/config, same as on the server.

### Authentication

Authentication is done with the JWT token issued from an api endpoint. Once the token is obtained it's saved in a config file. If the users starts the application the next time we try to read the token from said file and check if it's still valid. The bulk of this logic is in the `core.Auth` class.

### Recording

There are two recording strategies: `java` and `ffmpeg`. The first one failed miserably because of a two large overhead javas `Robot` class uses when capturing the pixels for the desktop. To avoid theses issues we're relying on the ffmpeg binary that the user has to preinstall on its machine. The strategy can be chosen in the app.conf file.

### Upload

We're accepting a 30% filesize increase because we're uploading the the recording as base64 encoded string. The reason is, that the validation for json bodies was already in place on the server and we wanted to avoid to waste too much time on the correct implementation of a `form/multipart` upload and validation system. 



