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
	var tableText = '<table align="center" id="songTable" width="500px">\
	<tr class="tableHead"><th></th><th>Track Title</th><th>Artist</th><th>Length</th></tr>';
	var numTracks = textObj.tracks.items.length;
	if (numTracks == 0) {
		document.getElementById("searchResults").innerHTML = "No songs found. Please try a different search.";
		$("#searchResults").slideDown("fast");
		return;
	}
	var i;
	for (i = 0; i<numTracks; i++) {
		var item = textObj.tracks.items[i];
		tableText += '<tr id="item' + i + '"><td class="searchRow"> \
		<img title="Preview" onclick="preview(\'' + item.preview_url + '\')" \
		alt="Preview" src="images/play.png"></td>' + 
		'<td>' + item.name + "</td>" +
		"<td>" + item.artists[0].name + "</td>" +
		"<td>" + msToLength(item.duration_ms) + "</td>" +
		'<td class="searchRow">\
		<img alt="Add song to playlist" title="Add song to playlist" \
		src="images/add.png" onclick="addSong(\'' + item.uri + '\', ' + i + ', ' + item.duration_ms + ', this)"></td></tr>';
	}
	tableText += '</table>';
	if (numTracks == 5) 
		tableText += '<button id="load" onclick="loadDoc(10)">Load more songs</button>';
	document.getElementById("searchResults").innerHTML = tableText;
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
function addSong(song, num, duration, r) {
	var name = "item" + num;
	var table = document.getElementById('playlistTable');
	document.getElementById('duration').style.visibility = "visible";
	document.getElementById('defPL').innerHTML = "";
	if (table.innerHTML == "No songs added.") 
		table.innerHTML = "";
	var tableRow = document.getElementById(name).innerHTML;
	var string = tableRow.substring(tableRow.indexOf("</td>") + 5, tableRow.lastIndexOf("<td"));
	table.innerHTML += string;
	var i = r.parentNode.parentNode.rowIndex;
    document.getElementById("songTable").deleteRow(i);
	var dur = lengthToMs(document.getElementById('durVal').innerHTML) + parseInt(duration);
	document.getElementById('durVal').innerHTML = msToLength(dur);
}

// Starts audio preview using url
function preview(url) {
	var audio = new Audio(url);
	audio.play();
}