package com.netgents.antelese

import org.specs._

object AnteleseSpec extends Specification {
  
  import Antelese.{copy => _copy, resources => _resources, _}
  import java.io.File
  import org.apache.tools.ant.types._

  "This spec" should {

    "execute echo task" in {
      echo('message -> "Hello World 1")
      echo('<> -> "Hello World 2", ignore, ignore)
      task("echo")('<> -> "Hello World 3")
    }

    "execute property task" in {
      property('name -> "name", 'value -> "Walter")
      echo('message -> "Hello ${name}")
    }

    "execute mkdir and delete tasks" in {
      mkdir('dir -> "tmp")
      mkdir('dir -> new File("tmp/tmp1"))
      mkdir('dir -> "tmp/tmp1/tmp2")

      delete(
        'fileset -> fileset(
          'dir -> new File("tmp/tmp1/tmp2"),
          'exclude -> ('name -> "**/*.java")))

      delete('fileset -> fileset('dir -> "tmp/tmp1", 'includes -> "**/*"))
      delete('dir -> "tmp")
    }

    "execute copy task using fileset" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'fileset -> fileset(
          'dir -> "src",
          'include -> List(('name, "**/*.scala"))))
      delete('dir -> "tmp")
    }

    "execute copy task using path" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'flatten -> "true",
        'path -> path('pathelement -> ('path, "project/build.properties"))) // non-existent "path" attribute, default to "<>" 
      delete('dir -> "tmp")
    }

    "execute copy task using selector" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'flatten -> "yes",
        'includeEmptyDirs -> false,
        'fileset -> fileset(
          'dir -> "src",
          'contains -> contains('text -> "AnteleseSpec", 'casesensitive -> "yes")))
      delete('dir -> "tmp")
    }

    "execute copy task using file resources" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'flatten -> "yes",
        'resources -> _resources(file('file -> "project/build.properties"))) // non-existent "resources" attribute, default to "<>" 
      delete('dir -> "tmp")
    }

    "execute copy task using file and url resources" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'flatten -> true,
        '<> -> _resources(
          '<> -> file('file -> "project/build.properties"),
          '<> -> url('url -> "http://ant.apache.org/index.html")))
      delete('dir -> "tmp")
    }

    "execute copy task using sort resources" in {
      mkdir('dir -> "tmp")
      _copy(
        'todir -> "tmp",
        'flatten -> "yes",
        '<> -> first(
          'count -> 2,
          '<> -> sort(List(rc_name(), fileset('dir -> "src", 'includes -> "**/*.scala")))))
      delete('dir -> "tmp")
    }

    "execute parallel task" in {
      parallel('<> -> echo('message -> "Foo", 'perform -> false))
      parallel(List(echo('message -> "Bar", 'perform -> false), echo('message -> "Baz", 'perform -> false)))
    }

    "execute pathconvert task using specific mappers" in {
      pathconvert(
        'property -> "x",
        'targetos -> "unix",
        'path -> ('path -> "Aj.Java"), // pathconvert has createPath() thus only (name, value) is required
        '<> -> chainedmapper(
          List(flattenmapper(), regexpmapper('from -> "a(.*)\\.java", 'to -> "\\1.java.bak", 'casesensitive -> "no"))))
      echo('message -> "result = ${x}")
    }

    "execute pathconvert task using general mappers" in {
      pathconvert(
        'property -> "x",
        'targetos -> "unix",
        'path -> ('path -> "Aj.Java"), // pathconvert has createPath() thus only (name, value) is required
        '<> -> chainedmapper(
          'mapper -> mapper('type -> "flatten"),
          'mapper -> mapper('type -> "regexp", 'from -> "a(.*)\\.java", 'to -> "\\1.java.bak")))
      echo('message -> "result = ${x}")
    }

    "execute loadfile task using specific filterchains" in {
      loadfile(
        'srcfile -> "project/build.properties",
        'property -> "head",
        'filterchain -> filterchain(headfilter('lines -> 1)))
      echo('<> -> "head = ${head}")
    }
  }
}
