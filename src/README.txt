This directory contains everything necessary to build the entire Colt distribution and download file from scratch.

1. Contents
=========================

- This file "README.txt"

- The source codes in package subdirectories.

- A directory "cern/colt/doc-files" containing documentation to be fed into javadoc. For example,
	- "docTitle.html" - The title to go onto the javadoc overview page (index.html).
	- "Overview.html" - The top entry point into javadoc trees. javadoc will transform this into "index.html".
	- "README.html" - Links to the javadoc overview page (index.html). Will be copied to the root of the download file so that 
			excited first-time-users have something to click onto and don't get a disappointing initial impression cause they can't find the docu.
	- etc.
- "stylesheet.css" - A stylesheet defining the presentation of javadoc.
- "GNUmakefile" - Does the whole job; requires gmake. 


2. Building the distribution
============================

This distribution was build with gmake 3.77 using SUN JDK 1.3.1 on Linux.
Unix:
	Building as-is should be no problem on UNIX flavours other than Linux and Solaris.
NT:
	The makefile won't run in a NT shell. On NT use CYGWIN B20 or higher.
	Use javac; jikes get's confused by the mixed pathnames ("dir1\dir2/dir3/file") that CygWin uses.
	
For the build to succeed, currently a few directories must exists.
Perhaps the best idea is to reproduce my directory tree, then there should be no problems:

TOPDIR = $(HOME)/coltdev
TOPDIR/src      - this directory you are looking at now, with all subdirs (docu cern/colt cern/jet corejava etc.). You already have this in place.
TOPDIR/docs/doc     - the javadoc will go here.
TOPDIR/classes      - the classes will go here.
TOPDIR/lib         - the final download file "colt1.0.3.zip" will go here, as well as some temporary zip and jar files (colt.jar, src.zip).

If you organize your dirs like that, the only thing to build the distribution is take the makefile and change the line

TOPDIR = $(HOME)/coltdev

to your topdir, for example:

TOPDIR = $(HOME)/java/stuff/colt



Now cd to TOPDIR/src and type

gmake all

And a long while later "colt1.0.3.zip" should pop up in TOPDIR/lib.
Perhaps I will improve the makefile one day, it's certainly not that elegant...

CAUTION: the makefile will delete everything in the directories TOPDIR/classes and TOPDIR/docs/doc
In order not to regret having made a build, make sure there is nothing valuable in there.


By the way, javadoc produces plenty of warnings of the form

	javadoc: warning - Tag @see: Class or Package not found: java.util.Arrays
	
This is due to a known bug of javadoc, but harmless. The only effect of this warning is that no hyperlink to this class is generated.
(This warning is reported if a class (or package) is referenced in a doc comment, but the class is *not* used in any part of the real Java code. Then javadoc can't find the referenced class.)


Enjoy,
Wolfgang.Hoschek@cern.ch