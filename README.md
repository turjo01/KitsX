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

Integrate KitsX into your own projects with our straightforward API.

### Maven Integration
Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>xyris-plugins</id>
        <url>https://xyris.fun/public/mvn/repo/</url>
    </repository>
</repositories>

<dependencies>
<dependency>
    <groupId>dev.darkxx</groupId>
    <artifactId>KitsX</artifactId>
    <version>1.0</version>
    <scope>provided</scope>
</dependency>
</dependencies>
```


### Gradle Integration
Add the repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven {
        url 'https://xyris.fun/public/mvn/repo/'
    }
}

dependencies {
    implementation 'dev.darkxx:KitsX:1.0'
}
```

## API Examples

First, declare a field for the KitsApiProvider:

```java
    private KitsApiProvider kitsApiProvider;
```

Next, initialize the KitsApiProvider:

```java
        kitsApiProvider = KitsApiProvider.init(this);
```

Here are some usage examples:

```java
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        
        // Load the kit for the player
        KitsAPI kitsAPI = kitsApiProvider.getKitsAPI();
        kitsAPI.load(player, "Kit 1");
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

Just to let you know, there's more API. these are just examples.