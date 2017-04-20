
function AlbumCtrl($scope,$timeout) {
    $scope.counter = 0;
    $scope.onTimeout = function(){
        $scope.counter++;
        mytimeout = $timeout($scope.onTimeout,1000);
    }
    var mytimeout = $timeout($scope.onTimeout,1000);
    
    $scope.stop = function(){
        $timeout.cancel(mytimeout);
    }
            
}
