var exec = require('cordova/exec');

var bkp = {
   getKey: function(fnSuccess, fnError, data, cert){
      exec(fnSuccess, fnError, "GetBKP", "getKey", [data, cert]);
   },
   
   getPKP: function(fnSuccess, fnError, data, cert){
      exec(fnSuccess, fnError, "GetPKP", "getPKP", [data, cert]);
   }   
};

module.exports = bkp;
