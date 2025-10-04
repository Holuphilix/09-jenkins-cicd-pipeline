import unittest
from src.app import sample_function

class TestApp(unittest.TestCase):
    def test_sample_function(self):
        self.assertEqual(sample_function(), "Hello, CI/CD!")

if __name__ == "__main__":
    unittest.main()
