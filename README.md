# command-base
a command line tool contains some basic functions in order to simplify coding while wanting to create a small command line tool.

The basic design and features include:
    * each command line 'command' contains two part, command name and command options; command name is a unique string within the applicaiton like 'version' or 'help'; and each command can have 0 to n options and each option itself has basic attributes including:
        ** name --- option name and it should be unique within the command, like 'jdbcurl', 'user'
        ** value --- the value of the option [can be empty if it's flag option]
      Example:
        if a command line tool is used for load csv file to database, its command could look like "csv2db -createdb -db <DB table name> [-delete <false|true>]"; it has one command name 'createdb' and two options 'db' and 'delete'; 'db' requires a database table name and 'delete' is an optional option and it has value either true or false.
    * self contain ---- using minimium library or dependency (so far the base just depends on SLF4J
    * the command option and its attributes are defined in definition (using Java API); the tool will render 'help content' or 'usage' automatically
