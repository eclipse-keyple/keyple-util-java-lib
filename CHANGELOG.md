# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- "CHANGELOG.md" file (issue [eclipse/keyple#6]).
- CI: Forbid the publication of a version already released (issue [#14]).
- JSON serializer/deserializer for `short` and `long` data types (issue [#17]).
- `isHexString` method in `Assert` class.
- `hexToByte` method in `ByteArrayUtil` class.
- `hexToShort` method in `ByteArrayUtil` class.
- `hexToInt` method in `ByteArrayUtil` class.
- `hexToLong` method in `ByteArrayUtil` class.
- `toHex` methods for `byte`, `short`, `int` and `long` data types in `ByteArrayUtil` class.
### Changed
- Optimizations of hexadecimal data conversions.

## [2.0.0] - 2021-10-06
This is the initial release.
It follows the extraction of Keyple 1.0 components contained in the `eclipse/keyple-java` repository to dedicated repositories.
It also brings many major API changes.

[unreleased]: https://github.com/eclipse/keyple-util-java-lib/compare/2.0.0...HEAD
[2.0.0]: https://github.com/eclipse/keyple-util-java-lib/releases/tag/2.0.0

[#17]: https://github.com/eclipse/keyple-util-java-lib/issues/17
[#14]: https://github.com/eclipse/keyple-util-java-lib/issues/14

[eclipse/keyple#6]: https://github.com/eclipse/keyple/issues/6