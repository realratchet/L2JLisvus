# L2JLisvus Chronicle 4: Scions of Destiny
[![GitLab latest release](https://badgen.net/gitlab/release/TheDnR/l2j-lisvus)](https://gitlab.com/TheDnR/l2j-lisvus/-/releases)
[![Website www.l2jlisvus.eu](https://img.shields.io/website-up-down-green-red/http/www.l2jlisvus.eu.svg)](https://www.l2jlisvus.eu/)
[![Discord](https://img.shields.io/discord/622789181973987338.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2)](https://discord.gg/nsw2s4G)
[![made-with-java](https://img.shields.io/badge/Made%20with-Java-1791ce.svg)](https://java.com/)

## Description
This is a fork of L2J server emulator.  
It's an open-source project that works with `Chronicle 4: Scions of Destiny` client.  
Written mostly in Java and maintained since 2011.

## Project status
I have been working on this project for years with the aid of its users and without profit.  
Real life responsibilities and the importance of time altogether have made me slow down development.  
On top of that, years have passed and game is old. Even so, I still apply improvements whenever I get some free time.

## Prerequisites
- Java 11
- Apache Ant
- MySQL or MariaDB
- Git

**Note:** An IDE may already include few of those.

## Development setup

### Eclipse for Java
To clone and add project to workspace, click on `File -> Import -> Git -> Projects from Git -> Clone URI` and paste project URI.  
To add a local repository to workspace, click on `File -> Import -> General -> Projects from Folder or Archive` and select directory to import. Then, click Finish.

### Visual Studio Code
For this editor, I suggest that you install `Extension Pack for Java` extension.  
Hit `git clone https://gitlab.com/TheDnR/l2j-lisvus.git` in your CLI.  
Once you have cloned repository, go to editor and click `File -> Open Folder` and select `l2j-lisvus` folder.

### Sublime Text 3
For this editor, the basic packages I suggest you to install are `SublimeLinter` along with its javac extension, `FileManager`, `GitSavvy`, and a terminal of your choice (e.g. `Terminus`).  
Hit `git clone https://gitlab.com/TheDnR/l2j-lisvus.git` in your CLI.  
Once you have cloned repository, navigate to `l2j-lisvus` folder and execute `l2j-lisvus.sublime-project` file.

**Note:** In Windows, linter of both editors will be quite slow to apply syntax highlighting. That is because project has so many files.  
If you plan on using files for development purposes, do yourself a favor and start using a linux distro.

## Usage
Builds are created using Ant builder. Both `core` and `datapack` folders contain a `build.xml` file for Ant to export build packages.
Most IDEs have built-in support for Ant Builds.

When it comes to editors, the easiest method to use Ant is to download and set it up, then use it in your CLI.  
Its usage is as easy as navigating into source directories by hitting `cd core` or `cd datapack` respectively, and hit `ant` command.  
You can also perform both builds from project root directory by hitting `ant -f core/build.xml` and `ant -f datapack/build.xml` commands.  
Build process exports a `build` directory that contains compiler output in various formats (e.g. zip).

Once builds are done for both `core` and `datapack`, you can create a folder for your server files (e.g. myfirstl2server) and extract `zip` archives from `build` folders.

## Server setup

### Database installation
Create an `sql` database using `mysql` CLI or GUI tools like `Navicat`, `HeidiSQL`, and `phpMyAdmin`.  
In you server folder, go to `tools` and run `database_installer` script. Follow the installer instructions to complete database setup.

Once your database setup is completed, go to `login` directory and run `RegisterGameServer` script.  
It will ask you to type a server ID of your choice (e.g. 1). When you do, hit enter and a `hexid.txt` file will be generated in the same folder.  
Move `hexid.txt` to `gameserver/config` directory.

### Account creation
You can create an account with admin rights easily. Go to `login` folder and run `startSQLAccountManager` script.
It will ask you to type account details along with access level (e.g. `100` for admins).

### Startup
Server environment consists of `login` and `game` servers. Both must be running to start server properly.  
To do this, run `startLoginServer` and `startGameServer` scripts.  
L2JLisvus server will then consist of 2 CLI consoles.  
Server startup will be completed once gameserver console has printed `Registered on login as Server...`.


## Contributing
I'll gladly accept merge requests provided that I agree to changes and those changes don't break any existing features.

## Support
The most desired method to report an issue is using gitlab's issue tracker.  
Otherwise, visit [forum](https://www.l2jlisvus.eu) or join [discord channel](https://discord.gg/nsw2s4G).

## License
Project is under the `GNU GPLv3`.

## Authors and acknowledgment
Special thanks goes to all those people who have supported the development of this project until now.
