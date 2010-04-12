#!/bin/sh
if test -d "$1"
then
  find "$1" -type f -print0 | grep -z -v .svn | grep -z -v clean.sh | grep -z -v setmime | xargs -0 rm -f
else
  echo "Please specify a directory to clean"
fi
