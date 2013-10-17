%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%                                                                    %%%
%%%   Class bestand Titelpagina voor Ontwerp van Software Systemen.   %%%
%%%                                                                    %%%
%%%   Opties:                                                          %%%
%%%     i1     Iteratie 1                                   %%%
%%%     i2     Iteratie 2                                   %%%
%%%     i3     Iteratie 3                                   %%%
%%%     eind    Eindverslag                                            %%%
%%%                                                                    %%%
%%%   17 Oktober 2013                                                %%%
%%%   Versie 1.0           
%%%   Sophie Marien                                            %%%
%%%                                                                    %%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesClass{oss}[2013/10/17 v1.0 Ontwerp van Software Systemen class]
\def\thetitle{}
\def\thepagelimit{100}
\DeclareOption{i1}{%
  \def\thetitle{Verslag: iteratie 1}%
  \def\thepagelimit{10}}
\DeclareOption{tt2}{%
  \def\thetitle{verslag: iteratie 2}%
  \def\thepagelimit{15}}
\DeclareOption{tt3}{%
  \def\thetitle{Verslag: iteratie 3}%
  \def\thepagelimit{20}}
\DeclareOption{eind}{%
  \def\thetitle{ Eindverslag}%
  \def\thepagelimit{30}}
\ProcessOptions
\LoadClass[10pt,a4paper]{article}
\RequirePackage[dutch]{babel}
\RequirePackage{color}
\RequirePackage{everypage}
\RequirePackage{geometry}
\RequirePackage{graphicx}
\geometry{margin=3cm}
\def\team#1{\def\theteam{#1}}
\def\members#1{\def\themembers{#1}}
\def\maketitlepage{%
  \begin{titlepage}%
    \begin{center}%
      \@makelogo%
      \vfill%
      {\Huge\sc Ontwerp van Software Systemen}\\[0.75cm]%
      {\huge\bf \thetitle}%\\[0.4cm]%
      \vfill%
%      \begin{minipage}{0.6\textwidth}%
 %      \begin{flushleft}%
  %        {\Large\em Team:} \\[6pt]%
 %         {\LARGE\bf \theteam}%
  %      \end{flushleft}%
 %   \end{minipage}\hfill%
      \begin{minipage}{0.8\textwidth}%
        \begin{flushright}%
         {\Large\em Team:} {\Large \sc \theteam} \\[6pt] %
          {\Large\sc \themembers\\}%
        \end{flushright}%
      \end{minipage}\\%
      \vspace{30mm}%
      {\large Academiejaar 2013 -- 2014}%
    \end{center}%
  \end{titlepage}%
}
\def\@makelogo{%
  \hfil%
  \begin{minipage}{0.1\textwidth}%
    \begin{flushleft}%
    \includegraphics[height=\logokuleng@height]{logokuleng}%
  \end{flushleft}%
  \end{minipage}\hfill%
%  \begin{minipage}{0.7\textwidth}%
 %   {\huge\sf KU~Leuven}\\[10pt]%
  %  {\LARGE\sf Departement Computerwetenschappen}%
 % \end{minipage}%
  \hfil%
}
\def\logokuleng@height{3.4cm}
\def\pagelimittext{PAGE LIMIT}
\def\pagelimitfontsize{2cm}
\def\pagelimitscale{5}
\newcommand\sc@wm@inbox[1]{%
  \setbox\@tempboxa\vbox to \z@{%
    \vskip -1in \moveleft 1in \vbox{%
      \hbox to \z@{%
        #1\hss}%
    }%
    \vss%
  }
  \dp\@tempboxa\z@
  \box\@tempboxa%
}
\newcommand\sc@wm@printtext[1]{%
  \setlength{\@tempdimb}{.5\paperwidth}%
  \setlength{\@tempdimc}{-.5\paperheight}%
  \setlength{\unitlength}{1pt}%
  \put(\strip@pt\@tempdimb,\strip@pt\@tempdimc){%
    \makebox(0,0){%
      \rotatebox{45}{\scalebox{\pagelimitscale}{%
        \textcolor[gray]{0.7}%
          {\fontsize{\pagelimitfontsize}{1.2\pagelimitfontsize}\selectfont{\pagelimittext}}%
      }}%
    }%
  }%
}
\AddEverypageHook{%
  \ifnum\thepage>\thepagelimit%
    \sc@wm@inbox{\sc@wm@printtext}
  \fi
}
\def\makeappendix{%
  \appendix%
  \addtocontents{toc}{\protect\setcounter{tocdepth}{1}}%
}
\endinput

