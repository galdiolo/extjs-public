#!/bin/sh
if test -d "$1"
then
  find "$1" -path '*/.svn' -prune -o -type f -exec rm {} +
else
  echo "Please specify a directory to clean"
fi
