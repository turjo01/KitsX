the best kits plugin for Crystal PvP servers

## Skript Support

```sk
on kit load:
  send "You loaded %kit%"
```

```sk
on kit save:
  send "You saved %kit%"
```

```sk
on kitroom open:
  send "You opened the kitroom"
```

## Developer API

### Maven

```xml
        <repository>
            <id>xyris-repo</id>
            <url>https://xyris.fun/repo/</url>
        </repository>

        <dependency>
            <groupId>dev.darkxx</groupId>
            <artifactId>KitsX</artifactId>
            <version>1.0.0</version>
            <scope>provided</scope>
        </dependency>
```

### Gradle

```groovy
repositories {
    maven("https://xyris.fun/repo/")
}

dependencies {
    compileOnly("dev.darkxx:KitsX:1.0.0")
}
```
---

First, initialize the KitsApiProvider in the `onEnable`

```java
        KitsApiProvider.init(this);
```

Next, get current instance of the KitsX api with the following:

```java
        KitsAPI kits = KitsApiProvider.get().getKitsAPI();
```

Here are some usage examples:

```java
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        
        // Load the kit for the player
        KitsAPI kits = KitsApiProvider.get().getKitsAPI();
        kits.load(p, "Kit 1");
    }

    @EventHandler
    public void onKitLoad(KitLoadEvent event) {
        Player player = e.getPlayer();
        String kitName = e.getKitName();
        
        // Send the player a message if the kit name equals to "Kit 1"
        if (kitName.equals("Kit 1")) {
            player.sendMessage("Hello, " + player.getName() + "! You loaded " + kitName + "!");
        }
    }
```