#!/usr/bin/env bash
mkdir outfile
cp @prj@-dist/@prj@-@pack@-service/appspec.yml outfile/
cp @prj@-dist/@prj@-@pack@-service/start.sh outfile/
cp @prj@-dist/@prj@-@pack@-service/validate.sh.etpl outfile/
unzip -d outfile/ @prj@-dist/@prj@-@pack@-service/target/@orgPath@.scm.@pack@.service.zip
