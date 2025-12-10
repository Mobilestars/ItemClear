# ItemClear

**ItemClear** is a simple Minecraft plugin for Spigot/Paper servers that automatically removes items from players' inventories after a configurable delay. This can help prevent item clutter and manage server performance.

---

## Features

- Automatically removes items when picked up.
- Automatically removes items when crafted.
- Configurable delay for item removal:
  - **Exact mode**: items are removed after a fixed time.
  - **Random mode**: items are removed after a random time between a minimum and maximum value.
- Works with any item type except air.
- Lightweight and easy to use.

---

## Installation

1. Download the latest release of **ItemClear**.
2. Place the `.jar` file into your server's `plugins` folder.
3. Start the server to generate the default `config.yml`.
4. Configure the plugin in `plugins/ItemClear/config.yml` as desired.
5. Restart or reload the server.

---

## Configuration

The configuration file (`config.yml`) contains the following options:

```yaml
# Mode of item removal
# "exact" = remove items after a fixed time
# "random" = remove items after a random time between min and max
mode: exact

# Used only in exact mode
exactTimeSeconds: 60

# Used only in random mode
randomMinSeconds: 30
randomMaxSeconds: 120
```
