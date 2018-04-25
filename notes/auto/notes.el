(TeX-add-style-hook
 "notes"
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
    "/Users/dennismuller/dotfiles/networkAssignmentConfig")
   (LaTeX-add-labels
    "sec:orgd639853"
    "sec:org7d7a9e6"
    "sec:org0041e00"
    "sec:orgf0412a9"
    "sec:org93ace6f"
    "sec:org11f3563"
    "sec:orgc78ecb4"
    "sec:org29c2ba8"
    "sec:orgfdfd3aa"
    "sec:orgcc3314e"
    "sec:org7c927b3"
    "sec:orgaf5a63b"
    "sec:orgd3a5b2d"
    "sec:org393d659"
    "sec:org9ea54c6"
    "sec:org0a1c43f"
    "sec:orgd2cdde2"
    "sec:org2938786"
    "sec:org3d3dca0"
    "sec:orgdeb052b"
    "sec:orgc19d2ea"
    "sec:org9a10a6c"
    "sec:org4afd87a"
    "sec:org350e527"
    "sec:orgfa9fd4b"
    "sec:orgb5557bc"
    "sec:org5420eeb"
    "sec:org962ce9e"
    "sec:org5263181"
    "sec:orge187f87"))
 :latex)

