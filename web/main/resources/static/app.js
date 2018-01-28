var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/command', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });

        stompClient.subscribe('/topic/customers', function (customers) {
            showCustomers(JSON.parse(customers.body));
        });

        stompClient.subscribe('/topic/block', function (block) {
            showBlock(JSON.parse(block.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendCommand() {
    stompClient.send("/app/command", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showCustomers(customers) {
    var html = customers.map(function(c) {
        return "<li>" + c.name + " (" + c.amount + ")" + "</li>";
    }).join("");
    console.log(html);
    $("#customers").html(html);
}

function showBlock(block) {
    console.info(block);
    var html = "<tr>";
    html += "<td>" + block.id + "</td>";
    html += "<td>" + block.timestamp + "</td>";
    html += "<td>" + block.prevHash + "</td>";
    html += "<td>" + block.hash + "</td>";
    html += "<td>" + block.nonce + "</td>";
    html += "</td>";
    $("#blocks").append(html);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendCommand(); });
});