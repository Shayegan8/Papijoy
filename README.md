This is a api for managing your config files
## syntax
the default splitor is @ you can change it 
also you can change getPropertiesList() splitor
<br>
example@example
<br>
* anlist<br>
0 - value1<br>
1 - value2<br>
* endif anlist

## Usage
The issue is about CompletableFuture<?> methods<br>
to use their result, for example you want check the property<br>
you need something like this<br>

above code will create a CompletableFuture<String> that inited by "ab" if the key wasn't exist else it will be the value with that key on that specific file<br>
then we want to use that value, thenAccept blocks the main thread you need to use the Bukkit.getScheduler().getMainThreadExecutor() or like above code run it<br>
asynchronously with bukkit scheduler and do your thing<br>
but sometimes you just want to show messages to players like when a player executed some command show the message to him with something like new Properties().getProperty() or spigot yml config methods<br>
BUT you don't want reload the config so you can use showProperty() method or showProperties() that helps you to do that
