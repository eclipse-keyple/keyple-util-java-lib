# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- Logging abstraction layer in the `org.eclipse.keyple.core.util.logging` package.
- `Logger` interface providing methods for trace, debug, info, warn, and error levels.
- `LoggerFactory` for centralized management of loggers and dynamic configuration of providers.
- `LoggerProvider` Service Provider Interface (SPI) to allow integration of various logging frameworks.
- Default temporary `Slf4jLogger` and `Slf4jLoggerProvider` for SLF4J v1.7.32 framework integration.
  This logger will be replaced by `NoOpLogger` and `NoOpLoggerProvider` on next major release.
### Changed
- Migrated the CI pipeline from Jenkins to GitHub Actions.

## [2.4.0] - 2024-04-12
### Added
- Class `ByteBufferJsonAdapter` for serialization of `ByteBuffer` objects.
### Changed
- Java source and target levels `1.6` -> `1.8`
- Renaming of `ThrowableJsonSerializer` -> `ThrowableJsonAdapter`
### Upgraded
- Gradle `6.8.3` -> `7.6.4`

## [2.3.1] - 2023-09-11
### Added
- Added project status badges on `README.md` file.
### Fixed
- Fixed compatibility issue with date formatting across Java versions in `DateJsonSerializer` and `CalendarJsonAdapter`
  classes (issue [#25]).
- CI: code coverage report when releasing.
### Upgraded
- "Google Gson Library" (com.google.code.gson) `2.8.8` -> `2.10.1` (source code not impacted)

## [2.3.0] - 2022-11-17
### Added
- `ByteArrayUtil.extractBytes` method to extract bytes from a number.
- `ByteArrayUtil.extractShort` method.
- `ByteArrayUtil.extractLong` method.
- `ByteArrayUtil.copyBytes` method.

## [2.2.0] - 2022-10-03
### Added
- Class `CalendarJsonAdapter` for serialization of `Calendar` objects in ISO 8601 format.
- Class `DateJsonSerializer` for serialization of `Date` objects in ISO 8601 format.

## [2.1.0] - 2022-05-30
### Added
- `CHANGELOG.md` file (issue [eclipse-keyple/keyple#6]).
- CI: Forbid the publication of a version already released (issue [#14]).
- JSON serializer/deserializer for `short` and `long` data types (issue [#17]).
- `Assert.isHexString` method.
- `HexUtil.isValid` method.
- `HexUtil.toByteArray` method.
- `HexUtil.toByte` method.
- `HexUtil.toShort` method.
- `HexUtil.toInt` method.
- `HexUtil.toLong` method.
- `HexUtil.toHex` methods for `byte[]`, `byte`, `short`, `int` and `long` data types.
- `ByteArrayUtil.extractBytes` method.
- `ByteArrayUtil.extractInt` method.
- `BerTlvUtil.parse` method to parse TLV data structure having several identical TAGs (issue [#19]).
### Changed
- Optimizations of hexadecimal data conversions.
### Deprecated
- `ByteArrayUtil.isValidHexString` method replaced by `HexUtil.isValid` method.
- `ByteArrayUtil.normalizeHexString` method.
- `ByteArrayUtil.fromHex` method replaced by `HexUtil.toByteArray` method.
- `ByteArrayUtil.toHex` method replaced by `HexUtil.toHex` method.
- `ByteArrayUtil.twoBytesToInt` method replaced by `ByteArrayUtil.extractInt` method.
- `ByteArrayUtil.twoBytesSignedToInt` method replaced by `ByteArrayUtil.extractInt` method.
- `ByteArrayUtil.threeBytesToInt` method replaced by `ByteArrayUtil.extractInt` method.
- `ByteArrayUtil.threeBytesSignedToInt` method replaced by `ByteArrayUtil.extractInt` method.
- `ByteArrayUtil.fourBytesToInt` method replaced by `ByteArrayUtil.extractInt` method.
- `ContactCardCommonProtocol` class.
- `ContactlessCardCommonProtocol` class.

## [2.0.0] - 2021-10-06
This is the initial release.
It follows the extraction of Keyple 1.0 components contained in the `eclipse-keyple/keyple-java` repository to dedicated 
repositories.
It also brings many major API changes.

[unreleased]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.4.0...HEAD
[2.4.0]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.3.1...2.4.0
[2.3.1]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.3.0...2.3.1
[2.3.0]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.2.0...2.3.0
[2.2.0]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.1.0...2.2.0
[2.1.0]: https://github.com/eclipse-keyple/keyple-util-java-lib/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/eclipse-keyple/keyple-util-java-lib/releases/tag/2.0.0

[#25]: https://github.com/eclipse-keyple/keyple-util-java-lib/issues/25
[#19]: https://github.com/eclipse-keyple/keyple-util-java-lib/issues/19
[#17]: https://github.com/eclipse-keyple/keyple-util-java-lib/issues/17
[#14]: https://github.com/eclipse-keyple/keyple-util-java-lib/issues/14

[eclipse-keyple/keyple#6]: https://github.com/eclipse-keyple/keyple/issues/6