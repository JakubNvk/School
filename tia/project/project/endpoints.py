from flask_restful import Resource


expeditions = {}

class MyExpeditions(Resource):
    def get(self, myexp_id):
        return {myexp_id: expeditions[myexp_id]}
