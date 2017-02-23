# command-base
## Introduction
a command line tool contains some basic functions in order to simplify coding while wanting to create a small command line tool.

## Basic Concept
The basic design and features include:
* each command line 'command' contains two part:
    + command name ---- each 'command' should have unique (witing the application scope) name
    + command options --- one 'command' could have 0 to n options
      - each option has a name (should be unique within the command scope) and 0 or 1 value; samples like:
        - "-debug" --- without value
        - "-file abc.xml" --- file option with value of 'abc.xml'
* both command name and options names have to be more than two characters like "help", "version"
* to better distingurish name vs its value, all command name and option name are using '-' as prefix, like '-help', '-file'; although it's not mantatory

## Feature of Command base
* each command can be defined via XML configuation or by API

  <i>Example of XML definition</i>
  ```xml
  <CommandApplication name="csv2db">
	<Command name="help" class="com.icedeer.common.cmd.ext.HelpCommand" index="5"/>
	<Command name="createdb" class="com.icedeer.csv2db.CreateDBCommand" index="1">
		<Option name="db" index="1" desc="DB table name" mandatory="true" hasValue="true"/>
		<Option name="delete" index="2" desc="true - to truncate existing table" mandatory="false" hasValue="true">
			<Value defaut="true">false</Value>
			<Value>true</Value>
		</Option>
	</Command>
  ...
  </CommandApplication>
  ```

  Or via <i>API</i> like:
  ```java
  AppArgsDefinition argsDef = getAppArgsDefinition();

  argsDef.addCommand("help");

  argsDef.addCommand("createdb");
  argsDef.addOption("createdb", "db", 1, true, true);
  argsDef.addOption("createdb", "delete", 2, false, true, "false");
  ```
* the tool will render out Command-line Usage (similar like help command does) basic on the command definition given above; no manual work here.
  ```
  demoApp Usage :

  -hello         -name <You name>
                 [-country <Canada|US>]

  -help

  ```

* while command-line arguments been parsed by the tool, it will do the basic validation (like required option, option with value should expect the value, etc); user can define additional customized validation rule by overwritten `Command.setAndValidateCommandOptions(CommandOption cmdOption)` method

* user can focus on implementation of each command class which should extends either `AbstractAppCommand` or `AbstractXmlConfCommand` if uses XML configuration; and in general just need to implement one method of 'execute()`;

* it has minimun dependency on other libraries; the most basic one just requires `SLF4J`
***
## How to start?
1) define a XML named `command_def.xml` with detail on what command (with its binding java class) and its options, for example, below XML defines two commands (`-help`) and (`-hello`); `help` is a built-in command, so no need to implement it; and `hello` will take two options: "name" which is mandatory option and "country" is optional option.
<code>
  ```xml
  <CommandApplication name="demoApp">
    <Command name="help" class="com.icedeer.common.cmd.ext.HelpCommand" index="2"/>
    <Command name="hello" class="com.icedeer.common.cmd.demo.HelloCommand" index="1">
      <Option name="name" index="1" desc="You name" mandatory="true" hasValue="true"/>
      <Option name="country" index="2" desc="from which country" mandatory="false" hasValue="true">
        <Value defaut="true">Canada</Value>
        <Value>US</Value>
      </Option>
    </Command>
  </CommandApplication>
  ```
2) create a java class of `com.icedeer.common.cmd.demo.HelloCommand` which looks like below:
```java
public class HelloCommand extends AbstractXmlConfCommand{

    public HelloCommand(String name) {
        super(name);
    }

    public void execute() throws Exception {
        String name = getCommandOption().getOptionValue("name");
        String country = getCommandOption().getOptionValue("country");

        logger.info("Your name is [{}] and your country is [{}]", name, country);

        System.out.println("Hello "+name +country == null ?"": " from "+country);

    }
}
```
3) run tool with main class of `com.icedeer.common.cmd.ext.XmlConfGenericApp` (you can extends this class with your implementation)
```java
  XmlConfGenericApp app = new XmlConfGenericApp();

  String[] args = new String[]{"-hello", "-name", "Peter", "-country", "US"};
  try {
      app.executeApp(args);

      assert(true);
  } catch (ArgumentValidationException e) {

      assert(false);
  } catch (Exception err){
      assert(false);
  }
```
