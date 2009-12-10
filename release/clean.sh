#!/bin/sh
find ./ -type f | grep -v .svn | grep -v clean.sh | grep -v setmime | xargs rm -f
