# Simple command line driven text search engine
The exercise is to write a command line driven text search engine that reads all the text files in the given directory, building an in memory representation of the files and their contents, and then gives a command prompt at which interactive searches can be performed.

The search should take the words given on the prompt and return a list of the top 10 (maximum)
matching filenames in rank order, giving the rank score against each match.
## How to run
### Start
```
sbt
> run directoryContainingTextFiles
```
### Quit
Type `:quit` to exit console application

### Example
```
sbt
> run /foo/bar
28 files read in directory /foo/bar
search> to be or not to be
/foo/bar/416.txt - 67%
/foo/bar/tr823.txt - 67%
/foo/bar/vol04.iss0064-0118.txt - 67%
/foo/bar/vol08.iss0001-0071.txt - 67%
/foo/bar/vol09.iss0050-0100.txt - 67%
/foo/bar/codegeek.txt - 50%
/foo/bar/203.txt - 33%
/foo/bar/204.txt - 33%
/foo/bar/adventur.txt - 33%
/foo/bar/ethics.txt - 33%
search> cats
/foo/bar/203.txt - 100%
/foo/bar/416.txt - 100%
/foo/bar/vol04.iss0064-0118.txt - 100%
search> foobar
no matches found
search> :quit
Good bye!
```