# Simple-Java-Updater-Tool
External or built-in tool for autoupdateing of software.

# Usage

Updater.jar then executed :
1 read from config.json "version" and "server" fields
2 then it download newconfig.json from using url provided in "server" field.
3 it check equality of "version" fields and if there exist difference,
further update proccess will be started.
4 it will download files frm urls provided in urls.
5 (optional) zip archives can be extracted
6 (optional) md5 checksum can be compared for integrity checking.

# Config.json fields

"version" = version number, build date, or any other integer value
"server" = url with newconfig.json correspond to server for updates, must contain encoded url
using tool like http://meyerweb.com/eric/tools/dencoder/ or something like that.
"md5" = md5 checksum or string "off" if no md5 checking needed.
"updaterUpdated" = if updater need to be updated itself, this flag must be set to true otherwise to false
"urls" = array of urls that need to be downloaded, every url need to be encoded, base correct syntax is :
"urls":["http%3A%2F%2Fexample%2Fupdate.zip","http%3A%2F%2Fexample%2Fupdate.zip","http%3A%2F%2Fexample%2Fupdate.zip"]
virtualy it dont have limit on how many files can be listed here.

#TODO
Simple GUI
Make it work with simple url without need them to be encoded
Working start of external programm
