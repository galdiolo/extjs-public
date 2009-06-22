#!/bin/sh
find ./ -type f | grep -v .svn | grep -v clean.sh | xargs rm -f
