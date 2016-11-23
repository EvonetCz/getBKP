var exec = require('cordova/exec');

var bkp = {
   getKey: function(fnSuccess, fnError, data, cert){
      exec(fnSuccess, fnError, "GetBKP", "list", [data, cert]);
   }
};

module.exports = printer;
