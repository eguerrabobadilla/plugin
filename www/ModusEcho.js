var exec = require('cordova/exec');

/*exports.new_activity= function (arg0, success, error) {
    exec(success, error, 'ModusEcho', 'new_activity', [arg0]);
};*/
exports.echo= function (arg0, success, error) {
    exec(success, error, 'ModusEcho', 'echo', [arg0]);
};