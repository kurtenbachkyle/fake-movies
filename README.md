# com.github.kurtenbachkyle.fakemovies

Searches movie titles for movies which are still valid after a letter is removed.

see my [resources/my-movies.txt](https://github.com/kurtenbachkyle/fake-movies/blob/master/resources/my-movies.txt)

## Usage

Requires a line delimited list of movies at resources/movies.list and dictonary files.  See [resources/resource-description.txt](https://github.com/kurtenbachkyle/fake-movies/blob/master/resources/resource-description.txt). I used popular movies from imdb and a dictionary file from sourceforge.  More details are in the resource description file.

to run for your own list open the repl and run:

(use '[com.github.kurtenbachkyle.fakemovies :as f])
(f/get-alt-movie-titles)

## License

Copyright © 2015 Kyle Kurtenbach

Distributed under the Eclipse Public License version 1.0
