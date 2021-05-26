# Name: Aleksi Hietala

# Topic: Weather application to display weather of your location.

- Display current weather
- Display forecast for one week
- Change location for forecast
- Use GPS location

## API used

- Weather API https://openweathermap.org/api
- Temporary key that you can use in evaluation / testing: 39600443ee5d6c37e1cc907eb6af0f92

# Target: Android/Kotlin

# Screencast

[Youtube](https://youtu.be/YZyO4E06L6s)

# Google Play link:

[Weather app](#) (Not published)

# Release1 features:

- Display current weather
- Display one week forecast
- Change location (not ready with new API call, does not actually fetch data from new city anymore)

## Release1 screenshot

![Release1 MainActivity](demoImages/mainActivityRelease1.jpg?raw=true)

# Release2 features:

- Change location by typing city name or by mobile location
- UI improvements
- Error messages for user (for example incorrectly typed cities)

## Release2 screenshots

![Release2 MainActivity](demoImages/mainActivityRelease2.jpg?raw=true)
![Release2 SettingsActivity](demoImages/settingsActivityRelease2.jpg?raw=true)

# Known bugs

- If application fails to find user location UI will not display any weather info
