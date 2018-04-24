(TeX-add-style-hook
 "customerfeedback"
 (lambda ()
   (add-to-list 'LaTeX-verbatim-environments-local "lstlisting")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "path")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "url")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "lstinline")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "nolinkurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperbaseurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperimage")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperref")
   (add-to-list 'LaTeX-verbatim-macros-with-delims-local "path")
   (add-to-list 'LaTeX-verbatim-macros-with-delims-local "lstinline")
   (TeX-run-style-hooks
    "/Users/dennismuller/dotfiles/articleLatexConfig")
   (LaTeX-add-labels
    "sec:org4718a3c"
    "sec:org62e7666"
    "sec:orgd256524"
    "sec:orga929d3d"
    "sec:org7a4b4f5"
    "sec:orgfcc43e2"
    "sec:org78edac0"
    "sec:org1dadec1"
    "sec:orgf91a813"
    "sec:orgab0c9bc"
    "sec:org2a4d070"
    "sec:orge4d1019"
    "sec:org98a956e"
    "sec:orgd208454"
    "sec:org9e85fe0"
    "sec:org6df4067"
    "sec:orgef07d7b"
    "sec:orge846696"
    "sec:org16ece02"
    "sec:orga8e28cf"))
 :latex)

