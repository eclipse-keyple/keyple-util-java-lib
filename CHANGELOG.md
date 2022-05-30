# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [2.1.0] - 2022-05-30
### Added
- "CHANGELOG.md" file (issue [eclipse/keyple#6]).
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
It follows the extraction of Keyple 1.0 components contained in the `eclipse/keyple-java` repository to dedicated repositories.
It also brings many major API changes.

[unreleased]: https://github.com/eclipse/keyple-util-java-lib/compare/2.1.0...HEAD
[2.1.0]: https://github.com/eclipse/keyple-util-java-lib/compare/2.0.0...2.1.0
[2.0.0]: https://github.com/eclipse/keyple-util-java-lib/releases/tag/2.0.0

[#19]: https://github.com/eclipse/keyple-util-java-lib/issues/19
[#17]: https://github.com/eclipse/keyple-util-java-lib/issues/17
[#14]: https://github.com/eclipse/keyple-util-java-lib/issues/14

[eclipse/keyple#6]: https://github.com/eclipse/keyple/issues/6