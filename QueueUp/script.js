$(document).ready(function(){
    $("#q").keypress(function(event){
		var keycode = (event.keyCode ? event.keyCode : event.which);
        if (keycode == '13')loadDoc(5);
    });
});

function loadDoc(limit) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var txt = JSON.parse(this.responseText);
			genSearchTable(txt);
       }
    };
	var link = "https://api.spotify.com/v1/search?q=" + document.getElementById('q').value + "&type=track&market=US&limit=" + limit;
    xhttp.open("GET", link, true);
    xhttp.send();
}

// Creates table from Spotify search object
function genSearchTable(textObj) {
	$("#searchResults").slideUp(0);
	var listText = '<ul id="searchResList" class="searchList">';
	var numTracks = textObj.tracks.items.length;
	if (numTracks == 0) {
		document.getElementById("searchResults").innerHTML = "No songs found. Please try a different search.";
		$("#searchResults").slideDown("fast");
		return;
	}
	var i;
	for (i = 0; i<numTracks; i++) {
		var item = textObj.tracks.items[i];
		listText += '<li id="item' + i + '" class="searchListItem">\
		<a href="javascript:void(0);" class="item_link"\
		onclick="addSong(\'' + item.uri + '\', ' + i + ', ' + item.duration_ms + ', this, \''+ item.name +'\')">\
		<span class="songTitle">' + item.name + '</span><br>\
		&emsp;' + item.artists[0].name + '</a></li>';
	}
	listText += '</li>';
	if (numTracks == 5)
		listText += '<button id="load" onclick="loadDoc(10)">Load more songs</button>';
	document.getElementById("searchResults").innerHTML = listText;
	$("#searchResults").slideDown("fast");


}

// Converts ms to string (mm:ss)
function msToLength(millis) {
  var minutes = Math.floor(millis / 60000);
  var seconds = ((millis % 60000) / 1000).toFixed(0);
  return minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
}

// Converts a string length (mm:ss) to ms
function lengthToMs(length) {
	if (length == "") return 0;
	var seconds = parseInt(length.substring(length.indexOf(":") + 1));
	length = length.substring(0, length.indexOf(":"));
	var minutes = parseInt(length);
	return minutes * 60000 + seconds * 1000;
}

 // Adds song (name, artist, length) to current playlist and removes from search table
function addSong(song, num, duration, r, title) {
	// Alert user to confirm add song
	if (!confirm("Queue '" + title + "' up?")) return;
	var name = "item" + num;
	// Update client-side playlist
	var list = document.getElementById('currentPL_List');
	document.getElementById('duration').style.visibility = "visible";
	document.getElementById('defaultPL').innerHTML = "";
	//if (table.innerHTML == "No songs added.")
		//table.innerHTML = "";
	var listItem = document.getElementById(name).innerHTML;
	list.innerHTML += '<li class="playlistItem">' + listItem + '</li>';
	var elem = document.getElementById(name);
	elem.parentNode.removeChild(elem);
	var dur = lengthToMs(document.getElementById('durVal').innerHTML) + parseInt(duration);
	document.getElementById('durVal').innerHTML = msToLength(dur);

	// Add to database
	var values = {
		'room' 	: document.getElementById('roomnum').innerHTML,
		'song' 	: song
	};
  alert(song);
  alert("calling checkifroomexists");
  checkIfRoomExists(values);

}

function checkIfRoomExists(values){
  $.ajax( { type : 'POST',
            data : values,
            url  : 'checkID.php',              // <=== CALL THE PHP FUNCTION HERE.
            success: function ( data ) {
              alert( data );               // <=== VALUE RETURNED FROM FUNCTION.
              var idData = JSON.parse(data);
              if(idData.isRoom==1){
                alert("Room exists");
                addSongToQueue(values);
              }
              else{
                return 0;
              }
            },
            error: function ( xhr ) {
              alert( "error" );
            }
          });
}

function addSongToQueue(values){
  $.ajax( { type : 'POST',
            data : values,
            url  : 'addSong.php',              // <=== CALL THE PHP FUNCTION HERE.
            success: function ( data ) {
              alert( data );               // <=== VALUE RETURNED FROM FUNCTION.
              var idData = JSON.parse(data);
              if(idData.songAdded==1){
                alert("song added");
              }
              return idData.isRoom;
            },
            error: function ( xhr ) {
              alert( "error" );
            }
          });
}

// Starts audio preview using url
function preview(url) {
	var audio = new Audio(url);
	audio.play();
}
