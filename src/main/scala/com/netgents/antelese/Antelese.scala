package com.netgents.antelese

import org.apache.tools.ant._
  import filters.{util => _, _}
  import taskdefs._
    import condition._
    import cvslib._
    import email._
  import types._
    import mappers._
    import resources._
      import comparators._
  import types.selectors._
    import modifiedselector._
  import util._

object Antelese {

  private val logger = new NoBannerLogger
  logger setMessageOutputLevel Project.MSG_INFO
  logger setOutputPrintStream System.out
  logger setErrorPrintStream System.err

  val project = new Project
  project.init
  project addBuildListener logger

  val ignore = ('ignore, "ignore this attribute")

  // BaseFilterReader
  def classconstants = new ClassConstants
  def concatfilter = new ConcatFilter
  def deletecharacters = new TokenFilter.DeleteCharacters
  def filterreader = new BaseParamFilterReader {}
  def escapeunicode = new EscapeUnicode
  def expandproperties = new ExpandProperties
  def _fixcrlf = new FixCrLfFilter
  def headfilter = new HeadFilter
  def linecontains = new LineContains
  def linecontainsregexp = new LineContainsRegExp
  def prefixlines = new PrefixLines
  def replacetokens = new ReplaceTokens
  def stripjavacomments = new StripJavaComments
  def striplinebreaks = new StripLineBreaks
  def striplinecomments = new StripLineComments
  def tabstospaces = new TabsToSpaces
  def tailfilter = new TailFilter
  def tokenfilter = new TokenFilter

  // DataType
  def dirset = initDataType(new DirSet)
  def filelist = initDataType(new FileList)
  def fileset = initDataType(new FileSet)
  def filterset = initDataType(new FilterSet)
  def filterchain = initDataType(new FilterChain)
  def filtermapper = initDataType(new FilterMapper)
  def mapper = initDataType(new Mapper(project))
  def path = initDataType(new Path(project))
  def patternset = initDataType(new PatternSet)
  def propertyset = initDataType(new PropertySet)
  def tarfileset = initDataType(new TarFileSet)
  def zipfileset = initDataType(new ZipFileSet)

  // FileNameMapper
  def chainedmapper = new ChainedMapper
  def compositemapper = new CompositeMapper
  def flattenmapper = new FlatFileNameMapper
  def globmapper = new GlobPatternMapper
  def identitymapper = new IdentityMapper
  def mergemapper = new MergingMapper
  def packagemapper = new PackageNameMapper
  def regexpmapper = new RegexpPatternMapper
  def unpackagemapper = new UnPackageNameMapper

  // Selector
  def and = initDataType(new AndSelector)
  def contains = initDataType(new ContainsSelector)
  def containsregexp = initDataType(new ContainsRegexpSelector)
  def date = initDataType(new DateSelector)
  def depend = initDataType(new DependSelector)
  def depth = initDataType(new DepthSelector)
  def different = initDataType(new DifferentSelector)
  def filename = initDataType(new FilenameSelector)
  def majority = initDataType(new MajoritySelector)
  def modified = initDataType(new ModifiedSelector)
  def none = initDataType(new NoneSelector)
  def not = initDataType(new NotSelector)
  def or = initDataType(new OrSelector)
  def present = initDataType(new PresentSelector)
  def selector = initDataType(new SelectSelector)
  def signedselector = initDataType(new SignedSelector)
  def size = initDataType(new SizeSelector)
  def `type` = initDataType(new TypeSelector)

  // Resource
  def bzip2resource = initDataType(new BZip2Resource)
  def difference = initDataType(new Difference)
  def file = initDataType(new FileResource)
  def files = initDataType(new Files)
  def first = initDataType(new First)
  def gzipresource = initDataType(new GZipResource)
  def intersect = initDataType(new Intersect)
  def javaresource = initDataType(new JavaResource)
  def last = initDataType(new Last)
  def propertyresource = initDataType(new PropertyResource)
  def resources = initDataType(new Resources)
  def restrict = initDataType(new Restrict)
  def sort = initDataType(new Sort)
  def string = initDataType(new StringResource)
  def tarentry = initDataType(new TarResource)
  def tokens = initDataType(new Tokens)
  def union = initDataType(new Union)
  def url = initDataType(new URLResource)
  def zipentry = initDataType(new ZipResource)

  // Resource Comparator
  def rc_content = initDataType(new Content)
  def rc_date = initDataType(new Date)
  def rc_exists = initDataType(new Exists)
  def rc_name = initDataType(new Name)
  def rc_reverse = initDataType(new Reverse)
  def rc_size = initDataType(new Size)
  def rc_type = initDataType(new Type)

  // Task
  def ant = initTask(new Ant)
  def antcall = initTask(new CallTarget)
  def antstructure = initTask(new AntStructure)
  def antversion = initTask(new AntVersion)
  def apply = initTask(new ExecuteOn)
  def apt = initTask(new Apt)
  def available = initTask(new Available)
  def buildnumber = initTask(new BuildNumber)
  def bunzip2 = initTask(new BUnzip2)
  def bzip2 = initTask(new BZip2)
  def checksum = initTask(new Checksum)
  def chmod = initTask(new Chmod)
  def concat = initTask(new Concat)
  def copy = initTask(new Copy)
  def cvs = initTask(new Cvs)
  def cvschangelog = initTask(new ChangeLogTask)
  def cvsversion = initTask(new CvsVersion)
  def cvspass = initTask(new CVSPass)
  def cvstagdiff = initTask(new CvsTagDiff)
  def defaultexcludes = initTask(new DefaultExcludes)
  def delete = initTask(new Delete)
  def dependset = initTask(new DependSet)
  def diagnostics = initTask(new DiagnosticsTask)
  def dirname = initTask(new Dirname)
  def ear = initTask(new Ear)
  def echo = initTask(new Echo)
  def exec = initTask(new ExecTask)
  def fail = initTask(new Exit)
  def filter = initTask(new Filter)
  def fixcrlf = initTask(new FixCRLF)
  def genkey = initTask(new GenerateKey)
  def get = initTask(new Get)
  def gunzip = initTask(new GUnzip)
  def gzip = initTask(new GZip)
  def _import = initTask(new ImportTask)
  def input = initTask(new Input)
  def jar = initTask(new Jar)
  def _java = initTask(new Java)
  def javac = initTask(new Javac)
  def javadoc = initTask(new Javadoc)
  def length = initTask(new Length)
  def loadfile = initTask(new LoadFile)
  def loadproperties = initTask(new LoadProperties)
  def loadresource = initTask(new LoadResource)
  def makeurl = initTask(new MakeUrl)
  def mail = initTask(new EmailTask)
  def mkdir = initTask(new Mkdir)
  def move = initTask(new Move)
  def nice = initTask(new Nice)
  def parallel = initTask(new Parallel)
  def patch = initTask(new Patch)
  def pathconvert = initTask(new PathConvert)
  def property = initTask(new Property)
  def record = initTask(new Recorder)
  def replace = initTask(new Replace)
  def resourcecount = initTask(new ResourceCount)
  def retry = initTask(new Retry)
  def rmic = initTask(new Rmic)
  def sequential = initTask(new Sequential)
  def signjar = initTask(new SignJar)
  def sleep = initTask(new Sleep)
  def sql = initTask(new SQLExec)
  def sync = initTask(new Sync)
  def tar = initTask(new Tar)
  def taskdef = initTask(new Taskdef)
  def tempfile = initTask(new TempFile)
  def touch = initTask(new Touch)
  def truncate = initTask(new Truncate)
  def tstamp = initTask(new Tstamp)
  def typedef = initTask(new Typedef)
  def untar = initTask(new Untar)
  def uptodate = initTask(new UpToDate)
  def war = initTask(new War)
  def whichresource = initTask(new WhichResource)
  def xmlproperty = initTask(new XmlProperty)
  def xslt = initTask(new XSLTProcess)
  def zip = initTask(new Zip)

  def task(name: String): Task = {
    val task = project.createTask(name)
    initTask(task)
  }

  def initDataType(dataType: DataType): DataType = {
    dataType setProject project
    dataType
  }

  def initTask(task: Task): Task = {
    task.init
    task setProject project
    task
  }

  implicit def enrichBaseFilterReader(baseFilterReader: BaseFilterReader) = new RichBaseFilterReader(project, baseFilterReader)
  implicit def enrichDataType(dataType: DataType) = new RichDataType(dataType)
  implicit def enrichFileNameMapper(fileNameMapper: FileNameMapper) = new RichFileNameMapper(project, fileNameMapper)
  implicit def enrichTask(task: Task) = new RichTask(task)
}
