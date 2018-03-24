#!/bin/sh
cd /Users/will/git/dotcms4/osgi/com.dotcms.visitor.filter/logs
rsync -avvz -e "ssh -i ~/keys/dotcms-corp-2017-08-0xF33F.pem" ec2-user@dotcms.com:/opt/dotcms/logs/dotcms.com/visit*.log .
