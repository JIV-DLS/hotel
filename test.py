import requests
import json

# Set the base URL for the Spring Boot API
base_url = "http://localhost:8081"

# Scenario 1: Create a new client
def create_client():
    endpoint = "/clients"
    client_data = {
        "fullName": "John Doe",
        "email": "john.doe@example.com",
        "phoneNumber": "+1234567890"
    }
    response = requests.post(base_url + endpoint, json=client_data)
    print("Scenario 1 - Create Client:")
    created_client = response.json()
    print(created_client)
    return created_client

def credit_account(client_id, amount, currency='DOLLAR'):

    endpoint = f"/clients/{client_id}/credit/{amount}/{currency}"
    response = requests.get(base_url + endpoint)
    print("Scenario - Credite client account:")
    credited = response.json()
    print(credited)
    return credited

def get_account_balance(client_id, currency='DOLLAR'):

    endpoint = f"/clients/{client_id}/balance/{currency}"
    response = requests.get(base_url + endpoint)
    print("Scenario - Credite client account:")
    credited = response.json()
    print(credited)
    return credited

# Scenario 2: Fetch clients
def fetch_clients():
    endpoint = "/clients"
    response = requests.get(base_url + endpoint)
    print("\nScenario 2 - Fetch Clients:")
    print(response.json())

# Scenario 3: Make a reservation
def make_reservation(client_id, room_type, number_of_nights=3, in_x_days=7):
    endpoint = "/reservations"
    reservation_data = {
        "clientId": client_id,  # Replace with an actual client ID
        "roomType": room_type,   # Replace with actual room IDs
        "checkInDate": (datetime.now().date() + timedelta(days=in_x_days)).,
        "numberOfNights": number_of_nights
    }
    response = requests.post(base_url + endpoint, json=reservation_data)
    print("\nScenario 3 - Make Reservation:")
    reservation_done = response.json()
    print(reservation_done)
    return reservation_done
# Scenario 3: Make a reservation
def confirm_reservation(reservationId):
    endpoint = f"/{reservationId}/confirm"
    response = requests.get(base_url + endpoint)
    print("\nScenario - Reservation confirmed:")
    reservation_confirmed = response.json()
    print(reservation_confirmed)
    return reservation_confirmed

# Scenario 4: Fetch reservations
def fetch_reservations():
    endpoint = "/reservations"
    response = requests.get(base_url + endpoint)
    print("\nScenario 4 - Fetch Reservations:")
    print(response.json())

# Scenario 5: Fetch rooms
def fetch_rooms():
    endpoint = "/rooms"
    response = requests.get(base_url + endpoint)
    print("\nScenario 5 - Fetch Rooms:")
    rooms = response.json()
    print(rooms)
    return rooms

# Execute the scenarios
client = create_client()
rooms = fetch_rooms()
assert credit_account(client['clientId'], 150)
reservation_1 = make_reservation(client['clientId'], rooms[0]['type'], 3)
assert get_account_balance(client['clientId']) == 150 - (rooms[0]['pricePerNight']*3)//2
assert confirm_reservation(reservation_1)
assert get_account_balance(client['clientId']) == 150 - (rooms[0]['pricePerNight']*3)
fetch_reservations()
