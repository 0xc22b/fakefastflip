<!doctype html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <title>Fake Fastflip</title>
        <style>
            body {
			    font-family: Verdana,Helvetica,Arial,Tahoma,FreeSans,sans-serif;
			    font-size: 12.5px;
			    font-size-adjust: none;
			    font-style: normal;
			    font-variant: normal;
			    font-weight: normal;
			}
			
            #noFrame {
                text-align: center;
                padding-top: 20%;
            }
        </style>
    </head>
<body>
    <div id='noFrame'></div>
    <script>
        (function() {
            setTimeout(function(){
                var noFrame = document.getElementById('noFrame');
                noFrame.innerHTML = 'This website might not allow to be embedded within an iFrame. Click the link above to open in a new tab.';
            }, 15000);
        }());
    </script>
</body>
</html>
