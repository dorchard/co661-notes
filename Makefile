default: quick

quick:
	pdflatex ccs-notes.tex

full:
	pdflatex ccs-notes.tex
	bibtex ccs-notes
	pdflatex ccs-notes.tex
	pdflatex ccs-notes.tex
