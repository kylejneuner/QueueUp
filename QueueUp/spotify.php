<html>
<title>QueueUp: Add Songs</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="script.js" defer></script>
<link rel="stylesheet" href="styles.css">

<body>
<div id="search" style="text-align:center">
	<h4 style="color:#555555">Room: <span id="roomnum" style="text-transform:uppercase"><?php echo $_GET['roomcode']?></span></h4>
	<h1>Search For Songs</h1>
	<small><i>Search for a song or artist on Spotify</i></small>
	<div>
		<input class="searchInput" type="text" size="40" id="q" name="q" placeholder="Search for a track..." autofocus>
		<button type="button" id="search" value="Search" onclick="loadDoc(5)">Search</button>
	</div>
</div>

<div id="searchResults" class="normalList"></div>

<br>
<div id="currentPL" class="normalList" style="text-align:center">
	<h2>Current Playlist</h2>
	<ul id="currentPL_List" class="searchList">
		<span id="defaultPL">No songs added.</span>
	</ul>
	<p id="duration" style="visibility:hidden">Playlist Duration: <span id="durVal"></span></p>
</div>

</body>

</html>