import time

from locust import HttpUser, between, task


class WebsiteUser(HttpUser):
    wait_time = between(5, 15)

    @task
    def index(self):
        uuid = str(time.time_ns())
        openId = str(time.time_ns())
        data = {
            'uuid': uuid,
            'imgName': uuid,
            'schoolName': uuid,
            'className': uuid,
            'studentNumber': '1',
            'otherInfo': uuid,
            'openId': openId
        }
        self.client.post("/graduation/admin/createBasicInfo", data)

# locust -f locusttest.py
