#
# @(#)GNUmakefile	2.0 2001/Sep/07
#
# Makefile for building the Colt distribution.
# Builds Colt, Jet, JAL, RngPack, VNI, CoreJava, util.concurrent, ViolinStrings, CLHEP. 
# More specifically, performs compilation, javadoc, jarring, download packaging.
#
# For building on Unix, see comments "For Unix".
# For building on Windows, see comments "For Windows". 
#
 
#
# Most frequently used targets:
#
#    all          -- clean javac jar doc distribute

#    clean        -- cleanClasses cleanDoc
#    cleanClasses -- remove old classes.
#    cleanDoc     -- remove old java doc.

#    javac        -- compile sources
#    doc      	  -- produce javadoc
#    jar          -- produce .jar file
#    distribute   -- combine sources, .jar, javadoc, etc. into a single download file.


# For Unix:
# change the next line and let it point to your JDK top directory (the one ABOVE bin)
JDK=/afs/cern.ch/sw/java/i386_redhat61/jdk/sun-1.3.1
#JDK=/afs/cern.ch/sw/java/sparc_solaris26/jdk/sun-1.3.1
# change the next line and let it point to your colt tree (the one ABOVE MySources)
TOPDIR = $(HOME)/coltdev

# For Windows:
# change the next line and let it point to your JDK top directory (the one ABOVE bin)
#JDK = /Applications/Compilers/Java/jdk1.2
#JDK = /Applications/Compilers/Java/jdk1.3
# change the next line and let it point to your colt tree (the one ABOVE MySources)
#TOPDIR = /MyStuff/Programming/Java
#TOPDIR = P:/.cern.ch/.user/h/hoschek/coltdev


JAVAC = javac
JAVADOC = javadoc
JAR = jar
#JAVAC = $(JDK)/bin/javac
#JAVADOC = $(JDK)/bin/javadoc
BUILDDIR=.

# Directory where the sources are to be found
SOURCEDIR = $(TOPDIR)/MySources

# Directory where the classes are to be put into
CLASSDIR = $(TOPDIR)/MyClasses

# Directory where the javadoc is to be put into
JAVADOCDIR = $(TOPDIR)/MyDocs

# Directory where the archives are to be put into
JARDIR = $(TOPDIR)/MyJars

# Directory where introduction, tutorials, executive summary, etc. are to be found
DOCUDIR = $(SOURCEDIR)/cern/colt/doc-files


# Libs for compilation
CLASSPATH = $(CLASSDIR):.:$(JDK)/jre/lib/rt.jar:$(JDK)/lib/tools.jar
#CLASSPATH = $(CLASSDIR)

#
# file names for .jar and archives.
#
VERSION = 1.0.2
DOWNLOADFILE = colt$(VERSION).zip
JARFILE = colt.jar
SRCFILE = src.zip


#
# javadoc parameters. 
#
# for Unix
DOCTITLE = '$(shell cat $(SOURCEDIR)/cern/colt/doc-files/docTitle.html)'
BOTTOM = '<font size="-1"><a href="mailto:wolfgang.hoschek@cern.ch">Submit a bug or feature</a>. Check the <a href="http://nicewww.cern.ch/~hoschek/colt/index.htm">Colt home page</a> for the latest news.</font>'

# for Windows
#DOCTITLE = "$(shell cat $(SOURCEDIR)/cern/colt/doc-files/docTitle.html)"
#BOTTOM = "<font size="-1"><a href="mailto:wolfgang.hoschek@cern.ch">Submit a bug or feature</a>. Check the <a href="http://nicewww.cern.ch/~hoschek/colt/index.htm">Colt home page</a> for the latest news.</font>"


HEADER = '<b>Colt $(VERSION)</b>'
WINDOWTITLE = 'Colt $(VERSION) - API Specification'
LINK = 'http://java.sun.com/products/jdk/1.3/docs/api'
OVERVIEW = cern/colt/doc-files/Overview.html
STYLESHEET = stylesheet.css


# For mif doclet
DOCLET_PATH=mifdoclet_C
#DOCLET_PATH=/MyStuff/Programming/Java/MySources/mifdoclet_C

FULL_DOCLET_PATH=$(SOURCEDIR)/$(DOCLET_PATH)
DOCTITLE_MIF = 'Open Source Libraries for High Performance Scientific and Technical Computing in Java'


#For javadoc 1.3, but the {@docRoot} feature is broken in javadoc 1.3 beta.
#HEADER = "<table border="0"> <tr> <td valign="top"><b>Colt 1.0</b></td> <td ><b> <img src="cern/colt/doc-files/logo.gif" width="55" height="38"> </b></td> </tr> </table>"
#BOTTOM = "<font size="-1"><a href="mailto:wolfgang.hoschek@cern.ch">Submit a bug or feature</a>. Check the <a href="http://nicewww.cern.ch/~hoschek/colt/index.htm">Colt home page</a> for the latest news. Read the <a href="{@docRoot}/cern/colt/doc-files/license.html">License & Copyright</a> statements.</font>"


#
# Sources with CERN copyright.
#
#MY_SOURCES = cern/colt/matrix 
MY_SOURCE_PACKAGES = cern/colt cern/colt/io cern/colt/function cern/colt/list cern/colt/list/adapter cern/colt/bitvector cern/colt/buffer cern/colt/map cern/colt/matrix cern/colt/matrix/impl cern/colt/matrix/linalg cern/colt/matrix/doublealgo cern/colt/matrix/objectalgo cern/colt/matrix/bench cern/jet/math cern/jet/random cern/jet/stat cern/jet/stat/quantile cern/jet/random/engine cern/jet/random/sampling cern/clhep
#MY_SOURCE_PACKAGES = cern/colt/matrix/linalg 

#
# Sources copy-righted by LGPL.
#
GNU_SOURCE_PACKAGES = hep/aida hep/aida/ref hep/aida/bin

#
# Sources copy-righted by third parties.
#
#OTHER_SOURCES = 
OTHER_SOURCE_PACKAGES = ViolinStrings edu/oswego/cs/dl/util/concurrent edu/oswego/cs/dl/util/concurrent/misc corejava com/imsl/math edu/cornell/lassp/houle/RngPack jal/BYTE jal/CHAR jal/DOUBLE jal/FLOAT jal/INT jal/LONG jal/Object jal/SHORT jal/String 


#
# Sources to compile.
#
COMPILE_SOURCES = $(MY_SOURCES) $(OTHER_SOURCES) 
COMPILE_SOURCE_PACKAGES = $(MY_SOURCE_PACKAGES) $(OTHER_SOURCE_PACKAGES) $(GNU_SOURCE_PACKAGES) 

#
# Classes to jar up into the distribution.
#
# cern/colt/PersistentObject --> cern/colt/PersistentObject.class
JAR_CLASSES = $(COMPILE_SOURCES:%=-C $(CLASSDIR) %.class)
#JAR_CLASS_PACKAGES = $(COMPILE_SOURCE_PACKAGES)
JAR_CLASS_PACKAGES = cern/clhep cern/colt cern/jet jal com/imsl/math edu/cornell/lassp/houle/RngPack corejava edu/oswego/cs/dl/util/concurrent ViolinStrings hep/aida

#
# Sources to jar up into the distribution.
#
# cern/colt/PersistentObject --> cern/colt/PersistentObject.java
JAR_SOURCES = $(COMPILE_SOURCES:%=-C $(SOURCEDIR) %.java)
JAR_SOURCES_PACKAGES = $(JAR_CLASS_PACKAGES)

#
# javadoc to produce and jar up into the distribution.
#
# cern/colt --> cern.colt
DOC_JAVA = $(shell echo $(COMPILE_SOURCES) | sed 's:\/:.:g')
DOC_PACKAGES = $(shell echo $(COMPILE_SOURCE_PACKAGES) | sed 's:\/:.:g')


#
# The rest of the makefile is driven by the parameterization done above.
# No user parameters to specify anymore (more or less).
#



# Prepare command line parameters for javac and jar:
# Text processing, substitution
JAVAC_PARAM_SOURCES = $(COMPILE_SOURCES:%=$(SOURCEDIR)/%.java) $(COMPILE_SOURCE_PACKAGES:%=$(SOURCEDIR)/%/*.java)
CLEAN_DEPENDENCIES = $(COMPILE_SOURCES:%=$(SOURCEDIR)/%.u) $(COMPILE_SOURCE_PACKAGES:%=$(SOURCEDIR)/%/*.u)
JAR_PARAM_CLASSES = $(JAR_CLASS_PACKAGES:%=-C $(CLASSDIR) %) 
JAR_PARAM_SOURCES = $(JAR_SOURCES_PACKAGES:%=-C $(SOURCEDIR) %) -C $(SOURCEDIR) README.txt GNUmakefile build manifest $(STYLESHEET) $(DOCLET_PATH)


# Prepare command line parameters for copyright license:
# Text processing, substitution
LICENSE_FILE = licenseTemplate
LICENSE_SOURCES = $(MY_SOURCES:%=$(SOURCEDIR)/%.java) $(MY_SOURCE_PACKAGES:%=$(SOURCEDIR)/%/*.java)
UNIQUE = "1999 CERN - European Organization for Nuclear Research."
#UNIQUE = "$(shell cat $(SOURCEDIR)/licenseTemplate)"

GNU_LICENSE_FILE = licenseTemplateGNU
GNU_LICENSE_SOURCES = $(GNU_SOURCE_PACKAGES:%=$(SOURCEDIR)/%/*.java)
UNIQUE_GNU = "This library is free software; you can redistribute it and"

# increment build number
BB = '$(shell cat build)'
BUILD = $(shell expr  $(BB) + 1)
VERSION_FILE = $(SOURCEDIR)/cern/colt/Version.java
LINES = $(shell egrep -c . $(VERSION_FILE))
LINES_TRIMMED = $(shell expr $(LINES) - 1)



#Not for release builds
#JIKES = /afs/cern.ch/sw/java/sparc_solaris26/opt/jikes-1.14/jikes
JIKES = /afs/cern.ch/sw/java/i386_redhat61/opt/jikes-1.14/jikes

# garbage
#PKGDIR		= $(shell echo $(PACKAGE) | sed 's:\.:/:g')
#SUBDIRS    = java javax sun sunw com corba mkdemo
#XXX = 50
#YYY = $(shell expr $(XXX) + 1)




####################################################
# Rules.
####################################################

all: clean javac jar doc distribute

clean: cleanClasses cleanDoc


#
# Removes old .class files.
#
cleanClasses:
	@echo "Deleting contents of class directory "$(CLASSDIR)" ..."
	rm -fr $(CLASSDIR)/*
	@rm -f $(SOURCEDIR)/cern/colt/list/ooObjArrayList.java
	@rm -f $(SOURCEDIR)/cern/colt/matrix/impl/FunctionDoubleMatrix1D.java
	@rm -fr $(SOURCEDIR)/cern/colt/benchmark/*
	@rm -fr $(SOURCEDIR)/cern/colt/benchmarkNinja/*
	@rm -fr $(SOURCEDIR)/cern/colt/benchmarkJama/*
	@rm -f $(SOURCEDIR)/cern/colt/matrix/doublealgo/Matrix2DModel.java
	@rm -f $(SOURCEDIR)/cern/colt/SmpSorter.java
	@rm -f $(SOURCEDIR)/cern/colt/Sort.java
	@rm -f $(SOURCEDIR)/cern/colt/Reverser.java


#
# Removes old java doc files.
#
cleanDoc:
	@echo "Deleting contents of javadoc directory "$(JAVADOCDIR)/doc" ..."
	rm -fr $(JAVADOCDIR)/doc/*

#
# Compiles .java to .class.
#
javac: incrementBuild
	@echo "Compilation can take a while (perhaps a minute)..."
	@echo $(JAVAC_PARAM_SOURCES) > tmpFiles938X
	$(JAVAC) -O -d $(CLASSDIR) -classpath $(CLASSPATH) @tmpFiles938X
	rm tmpFiles938X


#
# Produces javadoc from .java
#
doc:
	@echo "Producing java doc..."
	$(JAVADOC) -J-Xms16m -J-Xmx64m -public -sourcepath $(SOURCEDIR) -link $(LINK) -stylesheetfile $(SOURCEDIR)/$(STYLESHEET) 	-group 'Colt Packages' 'cern.colt*' -group 'Jet Packages' 'cern.jet*' -group 'Aida Histogram and CLHEP Packages' 'hep.aida*:cern.clhep*' -group 'Concurrent, CoreJava, RngPack, VNI and ViolinStrings Packages' 'edu.oswego.cs.dl.util.concurrent:corejava:edu.cornell.lassp.houle.RngPack:com.imsl*:ViolinStrings' -group 'JAL Packages' 'jal.BYTE:jal.CHAR:jal.DOUBLE:jal.FLOAT:jal.INT:jal.LONG:jal.Object:jal.SHORT:jal.String' -d $(JAVADOCDIR)/doc -overview $(OVERVIEW) -use -splitIndex -windowtitle $(WINDOWTITLE) -doctitle $(DOCTITLE) -header $(HEADER) -bottom $(BOTTOM) $(DOC_JAVA) $(DOC_PACKAGES) 

#
# Combines classes into .jar file.
#
jar:
	@echo "Now building .jar file for $(VERSION).$(BUILD) (`date`)..."
	@cp manifest tmp
	@echo "Implementation-Version: $(VERSION).$(BUILD) (`date`)" >> tmp
	$(JAR) cvfm $(JARDIR)/$(JARFILE) tmp $(JAR_CLASSES) $(JAR_PARAM_CLASSES)
	$(JAR) -i $(JARDIR)/$(JARFILE)
#	@rm tmp
# use jar -i only for >= jdk1.3 


#
# Combines sources into 1 file, then combine the single source file, the single .jar file and all javadoc into one single download file.
# Add some more stuff: README
#
distribute:
	@echo "Now building download file..."
	$(JAR) Mcvf $(JARDIR)/$(SRCFILE) $(JAR_SOURCES) $(JAR_PARAM_SOURCES) 
	$(JAR) Mcvf $(JARDIR)/$(DOWNLOADFILE) -C $(DOCUDIR) README.html -C $(JAVADOCDIR) doc -C $(JARDIR) $(JARFILE) -C $(JARDIR) $(SRCFILE) 

#
# increment build number
#
incrementBuild:
	@echo $(BUILD) > build
	@echo 'Version is $(VERSION).$(BUILD)'
	
#
# display current version
#
version:
	@echo 'Version last build was $(VERSION).$(BUILD)'
	@echo 'Version currently is'
	@$(JAVA) -jar $(JARDIR)/$(JARFILE)
	
license:
	@echo 'dummy' > tmp
	@for i in $(LICENSE_SOURCES) ; do \
	    if grep $(UNIQUE) $$i > /dev/null; then \
	    echo ">>>Already licensed "$$i" ..."; \
	    else \
	    echo ">>>Licensing "$$i" ..."; \
	    cp $$i tmp; \
	    cat $(LICENSE_FILE) tmp > $$i; \
	    fi; \
	done; \
	rm tmp

gnulicense:
	@echo 'dummy' > tmp
	@for i in $(GNU_LICENSE_SOURCES) ; do \
	    if grep $(UNIQUE_GNU) $$i > /dev/null; then \
	    echo ">>>Already GNU licensed "$$i" ..."; \
	    else \
	    echo ">>>GNU Licensing "$$i" ..."; \
	    cp $$i tmp; \
	    cat $(GNU_LICENSE_FILE) tmp > $$i; \
	    fi; \
	done; \
	rm tmp


#
# Produces printer-friendly docu by using Sun's MIF doclet (.java -> .mif -> .fm -> .ps -> .pdf)
# Requires the MIFdoclet and FrameMaker being installed (Solaris or NT) and fmbatch (part of FrameMaker) being in current PATH.
# We do this on Solaris. Maybe this also works on NT, but was not tested.
# See http://java.sun.com/products/jdk/javadoc/mifdoclet
#
# Currently the whole thing runs through, but in the very end reports an error that seems to be harmless and can be ignored.
# When the make rule is finished, do the following
# 1. Open $(JARDIR)/mif/spec.book in FrameMaker.
# 2. Menu File/Rearrange Files and move Overview before Contents, and cern.clhep downwards, just before package jet.
# 3. Menu File/Generate-Update, choose index and contents for the update. FrameMaker may report an "Unresolved Cross Ref", but that's not serious.
# 4. Menu File/Print and choose destination=single postscript file or single pdf file, paper size=A4 Letter, generate acrobat data=true.
# 5. Now you should have spec.ps or spec.pdf in directory $(JARDIR)/mif
#
docMif ::
	@echo "Producing printer-friendly MIF doc, when finished use FrameMaker to convert .mif to .ps or .pdf..."
	$(JAVADOC) -doclet com.sun.tools.doclets.mif.MIFDoclet \
	-docletpath $(FULL_DOCLET_PATH)/mif.jar:$(FULL_DOCLET_PATH)/mifoutput.jar:$(FULL_DOCLET_PATH)/standarddoclet.jar \
	-sourcepath $(SOURCEDIR) \
	-includedir $(FULL_DOCLET_PATH) \
	-public \
	-overview $(OVERVIEW) \
	-doctitle $(DOCTITLE_MIF) \
	-group 'Colt Packages' 'cern.colt*' -group 'Jet Packages' 'cern.jet*' -group 'Aida Histogram and CLHEP Packages' 'hep.aida*:cern.clhep*' -group 'Concurrent, CoreJava, RngPack and VNI Packages' 'edu.oswego.cs.dl.util.concurrent:corejava:edu.cornell.lassp.houle.RngPack:com.imsl*' -group 'JAL Packages' 'jal.BYTE:jal.CHAR:jal.DOUBLE:jal.FLOAT:jal.INT:jal.LONG:jal.Object:jal.SHORT:jal.String' \
	-membersummary compact \
	-bookfile spec.book \
	-batch fmbatch \
	-titlefile $(FULL_DOCLET_PATH)/titlepage2.mif \
	-contentsfile $(FULL_DOCLET_PATH)/contents.mif \
	-indexfile $(FULL_DOCLET_PATH)/index.mif \
	-d $(JARDIR)/mif \
	$(DOC_PACKAGES)


#
# Compiles .java to .class (less stable but faster than javac)
#
jikes: incrementBuild
	$(JIKES) -nowarn -classpath $(CLASSPATH) -d $(CLASSDIR) $(JAVAC_PARAM_SOURCES)


#
# Backup sources for safety.
#
backup:
	rm -fr $(TOPDIR)/src.bak
	cp -r $(SOURCEDIR) $(TOPDIR)/src.bak


#
# Throws away current sources and goes back to previously backup'ed sources.
#
restore:
	rm -fr $(TOPDIR)/tmp
	mv $(SOURCEDIR) $(TOPDIR)/tmp
	mv $(TOPDIR)/src.bak $(SOURCEDIR)

#
# for quick and dirty compilation only
#
jik:
	$(JIKES) -classpath $(CLASSPATH):$(HOME)/coltb4/myjars/colt.jar:$(HOME)/coltb4/myjars/array.zip:$(HOME)/coltb4/myclasses:$(CLASSPATH) -d $(HOME)/coltb4/myclasses $(SOURCEDIR)/cern/colt/matrix/bench/*.java $(SOURCEDIR)/demo2/*.java 

#
# for quick and dirty compilation only
#
io:
	$(JAVAC) -classpath $(CLASSPATH) -d $(HOME)/coltb4/myclasses $(SOURCEDIR)/java/io/*.java
	$(JAR) cvf $(HOME)/coltb4/myjars/io.jar -C $(HOME)/coltb4/myclasses java

#
# What follows is old stuff - No more used
#

# for quick and dirty tests only
copyjar:
	cp $(JARDIR)/$(JARFILE) $(HOME)/coltb4/myjars/$(JARFILE)

# for quick and dirty compilation only
jikbt:
	$(JIKES) -classpath /usr/local/libexec/jdk/1.2.2/jre/lib/rt.jar:$(HOME)/coltb4/myclasses:$(CLASSPATH) -d $(HOME)/coltb4/myclasses $(SOURCEDIR)/com/ibm/apps/*.java $(SOURCEDIR)/com/ibm/samples/*.java $(SOURCEDIR)/com/ibm/javaSpy/*.java $(SOURCEDIR)/com/ibm/samples/spySamp0/*.java $(SOURCEDIR)/com/ibm/samples/spySamp1/*.java $(SOURCEDIR)/com/ibm/samples/spySamp2/*.java $(SOURCEDIR)/com/ibm/subjects/*.java 
	
# for quick and dirty compilation only
jikbench:
	$(JAVAC) -classpath /usr/local/libexec/jdk/1.2.2/jre/lib/rt.jar:$(SOURCEDIR)/jgf/v20:$(CLASSPATH) $(SOURCEDIR)/jgf/v20/jgfutil/*.java 
	$(JAVAC) -classpath /usr/local/libexec/jdk/1.2.2/jre/lib/rt.jar:$(SOURCEDIR)/jgf/v20:$(CLASSPATH) $(SOURCEDIR)/jgf/v20/section1/*.java  
	$(JAVAC) -classpath /usr/local/libexec/jdk/1.2.2/jre/lib/rt.jar:$(SOURCEDIR)/jgf/v20:$(CLASSPATH) $(SOURCEDIR)/jgf/v20/section2/*.java $(SOURCEDIR)/jgf/v20/section2/crypt/*.java $(SOURCEDIR)/jgf/v20/section2/fft/*.java $(SOURCEDIR)/jgf/v20/section2/heapsort/*.java $(SOURCEDIR)/jgf/v20/section2/lufact/*.java $(SOURCEDIR)/jgf/v20/section2/series/*.java $(SOURCEDIR)/jgf/v20/section2/sor/*.java $(SOURCEDIR)/jgf/v20/section2/sparsematmult/*.java  
	
	
# Remove dependency files generated by jikes.
#
cleanDependencies:
	@for i in $(CLEAN_DEPENDENCIES) ; do \
	    echo ">>>del "$$i" ..."; \
	    rm $$i; \
	done;
