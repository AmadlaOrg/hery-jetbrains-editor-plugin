# HERY - JetBrains IDE Plugin

Language support for [HERY](https://github.com/AmadlaOrg/hery) (Hierarchical Entity Relational YAML) files in IntelliJ IDEA, GoLand, PyCharm, WebStorm, and other JetBrains IDEs.

## Features

### File Type Recognition
- `.hery` files registered as a distinct language
- Custom file icon

### Syntax Highlighting
- HERY reserved properties (`_type`, `_extends`, `_meta`, `_body`, `_requires`) highlighted as keywords
- Entity URIs with version detection
- Full YAML syntax (inherited from bundled YAML plugin)
- Color settings page under Settings > Editor > Color Scheme > HERY

### Code Completion
- Reserved properties at root level (`_type`, `_extends`, `_meta`, `_body`, `_requires`)
- Common `_meta` keys (`name`, `description`, `category`, `tags`)
- Standard entity type URIs (`amadla.org/entity/application@v1.0.0`, etc.)

### Live Templates
| Abbreviation | Description |
|---|---|
| `hery` | New entity with type, meta, and body |
| `hery-min` | Minimal entity (type + body) |
| `hery-extends` | Entity with `_extends` inheritance |
| `hery-requires` | Entity with `_requires` dependencies |
| `hery-full` | Entity with all five reserved properties |
| `hery-package` | Package entity |
| `hery-template` | Template entity for weaver |
| `hery-vm` | VM infrastructure entity |
| `_type` | Insert `_type` property |
| `_extends` | Insert `_extends` property |
| `_meta` | Insert `_meta` block |
| `_body` | Insert `_body` block |
| `_requires` | Insert `_requires` block |

### Structure View
- Navigable tree showing root-level keys and nested structure
- `_type` and `_extends` show their URI values inline

### Inspections
- Warning for unknown `_`-prefixed properties at root level
- Warning for reserved properties (`_type`, `_body`, etc.) used inside `_body` or `_meta`

### Other
- Line commenting with `#`
- Brace matching for `{}` and `[]`
- Indent-based code folding

## Requirements

- JetBrains IDE 2024.3+
- YAML plugin (bundled)

## Building

```bash
./gradlew buildPlugin
```

The plugin ZIP will be at `build/distributions/hery-jetbrains-editor-plugin-0.1.0.zip`.

## Installation

### From Disk
1. Build the plugin (see above)
2. In your IDE: Settings > Plugins > Gear icon > Install Plugin from Disk
3. Select the ZIP from `build/distributions/`
4. Restart the IDE

### Development
```bash
./gradlew runIde
```
This launches a sandboxed IDE instance with the plugin installed.

## HERY Format

HERY files are YAML documents with five reserved root-level properties:

```yaml
---
_type: amadla.org/entity/application@v1.0.0
_extends: github.com/AmadlaOrg/EntityApplication@v1.0.0
_meta:
  name: MyApp
  description: Application definition
  tags:
    - web
_requires:
  - amadla.org/entity/package@v1.0.0
_body:
  server_name: localhost
  port: 8080
```

See the [HERY specification](https://github.com/AmadlaOrg/hery) for full details.

## License

MIT
