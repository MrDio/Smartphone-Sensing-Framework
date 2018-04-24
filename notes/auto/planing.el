(TeX-add-style-hook
 "planing"
 (lambda ()
   (add-to-list 'LaTeX-verbatim-environments-local "lstlisting")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperref")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperimage")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperbaseurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "nolinkurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "lstinline")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "url")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "path")
   (add-to-list 'LaTeX-verbatim-macros-with-delims-local "lstinline")
   (add-to-list 'LaTeX-verbatim-macros-with-delims-local "path")
   (TeX-run-style-hooks
    "/Users/dennismuller/dotfiles/networkAssignmentConfig")
   (LaTeX-add-labels
    "sec:org2610146"
    "sec:org945b9f4"
    "sec:org34def69"
    "sec:orge43b7b8"
    "sec:org3b95898"
    "sec:orgd85891f"
    "sec:orga2926ad"
    "sec:orgd7a0521"
    "sec:org1cd7898"
    "sec:orgdf376cf"
    "sec:orgfd41c27"
    "sec:org073d37f"
    "sec:orgd44eac6"
    "sec:org4d440ca"
    "sec:orgc8e70b4"
    "sec:orgb3edcb5"
    "sec:orgd852888"
    "sec:org97d117c"
    "sec:org6af4132"
    "sec:orgeccf6fd"
    "sec:orge41382f"
    "sec:orged4964c"
    "sec:org2364917"
    "sec:org2012d7b"
    "sec:org14b7883"
    "sec:org20c4fcd"
    "sec:orgc4fa354"
    "sec:org903f351"
    "sec:orgaf1de29"
    "sec:org0528d08"
    "sec:orgcba1be4"
    "sec:org896d172"
    "sec:orgaa7b811"
    "sec:org0a88fdc"
    "sec:org8452a3e"))
 :latex)

