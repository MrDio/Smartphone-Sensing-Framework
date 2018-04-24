(TeX-add-style-hook
 "notes"
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
    "sec:org2c9ed28"
    "sec:orgd208c4b"
    "sec:org210bdc0"
    "sec:org86b9b52"
    "sec:orge6ac6ca"
    "sec:org493528f"
    "sec:org463bcd9"
    "sec:org01eb3ff"
    "sec:orga08a9a1"
    "sec:org9e5e015"
    "sec:org56b6177"
    "sec:orgee0256e"
    "sec:orgf213c4e"
    "sec:org8b90d67"
    "sec:orgda4522d"
    "sec:org624e913"
    "sec:orga3fb9a6"
    "sec:orgd655d2f"
    "sec:org0929265"
    "sec:org6306cc8"
    "sec:orgeae9f73"
    "sec:orgb2b1f54"
    "sec:orgdd19366"
    "sec:orgbb94b3e"
    "sec:orge379dce"
    "sec:orga2e3370"
    "sec:org7f5b138"))
 :latex)

