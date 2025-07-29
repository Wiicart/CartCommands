# CartCommands
A simple tree-based command framework for Bukkit and its forks.

## Use
Extend the CommandTree class and use CommandTree#builder() 
to build the root node for the protected constructor.

CommandTree extends CommandExecutor, so you register the Command the 
traditional Bukkit way.

## Maven
```
<dependency>
    <groupId>net.wiicart.commands</groupId>
    <artifactId>cart-commands</artifactId>
    <version>1.0</version>
</dependency>
```