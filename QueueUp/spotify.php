<html>
<title>QueueUp: Add Songs</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="script.js" defer></script>
<link rel="stylesheet" href="styles.css">

<body>
<div id="search" style="text-align:center">
	<h4 style="color:#555555">Room: <span style="text-transform:uppercase"><?php echo $_GET['roomcode']?></span></h4>
	<h1>Search For Songs</h1>
	<small><i>Search for a song or artist on Spotify</i></small>
	<div>
		<input type="text" size="40" id="q" name="q" placeholder="Search for a track..." autofocus>
		<button type="button" id="search" value="Search" onclick="loadDoc(5)">Search</button>
	</div><br>
</div>

<div id="searchResults" style="display:none; text-align:center;"></div>
<br><br>
<div id="currentPL" style="text-align:center">
	<h2>Current Playlist</h2>
	<table id="playlistTable" align="center">
		<span id="defPL">No songs added.</span>
	</table>
	<p id="duration" style="visibility:hidden">Playlist Duration: <span id="durVal"></span></p>
</div>

</body>

</html>