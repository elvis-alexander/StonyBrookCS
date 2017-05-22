import random
import xlsxwriter
import math


class Simulation:
    def __init__(self, prob, avg_dropped, avg_forwarded):
        self.prob = prob
        self.avg_dropped = avg_dropped
        self.avg_forwarded = avg_forwarded


class Model:
    def __init__(self, prob, forwarded):
        self.prob = prob
        self.forwarded = forwarded

    @staticmethod
    def model_equation(self, p):
        return 3 - (3 * math.pow(1-p, 10)) - (20 * p * (math.pow(1 - p, 9))) - (45 * math.pow(p, 2) * math.pow(1-p, 8))


class PacketSimulation:
    NUMBER_OF_SIMULATIONS = 10000
    PROBABILITY_INCREMENT = 0.02
    NUMBER_OF_SLOTS = 10

    def __init__(self):
        self.p = 0.02
        self.simulationList = []
        self.modelEquationList = []

    def create_excel(self):
        # excel file attributes
        workbook = xlsxwriter.Workbook('packet_simulation.xlsx')
        worksheet = workbook.add_worksheet()
        bold = workbook.add_format({'bold': 1})

        # graph data
        headings = ['Probability', 'Avg Busy Spots', 'Avg Dropped Spots', 'Model Equation']
        data = []
        prob_list = []
        avg_forwarded_list = []
        avg_dropped_list = []
        forwarded_list = []
        for index in range(len(self.simulationList)):
            prob_list.append(self.simulationList[index].prob)
            avg_forwarded_list.append(self.simulationList[index].avg_forwarded)
            avg_dropped_list.append(self.simulationList[index].avg_dropped)
            forwarded_list.append(self.modelEquationList[index].forwarded)

        data.append(prob_list)
        data.append(avg_forwarded_list)
        data.append(avg_dropped_list)
        data.append(forwarded_list)

        # add data to excel
        worksheet.write_row('A1', headings, bold)
        worksheet.write_column('A2', data[0])
        worksheet.write_column('B2', data[1])
        worksheet.write_column('C2', data[2])
        worksheet.write_column('D2', data[3])

        # graph 1 (busy vs. prob)
        chart1 = workbook.add_chart({'type': 'line'})
        chart1.add_series({
            # title,x-axis, y-axis
            'name':       '=Sheet1!$B$1',
            'categories': '=Sheet1!$A$2:$A$51',
            'values':     '=Sheet1!$B$2:$B$51',
        })
        chart1.set_title({'name': 'Avg # of Busy Spots vs Probability'})
        chart1.set_x_axis({'name': 'Probability'})
        chart1.set_y_axis({'name': 'Avg # of Busy Spots'})
        chart1.set_style(10)

        # graph 2 dropped vs prob
        chart2 = workbook.add_chart({'type': 'line'})
        chart2.add_series({
            'name':       '=Sheet1!$C$1',
            'categories': '=Sheet1!$A$2:$A$51',
            'values':     '=Sheet1!$C$2:$C$51',
        })
        chart2.set_title ({'name': 'Avg # of Dropped vs Probability'})
        chart2.set_x_axis({'name': 'Probability'})
        chart2.set_y_axis({'name': 'Avg # of Dropped'})
        chart2.set_style(10)

        # graph 3 dropped vs prob
        chart3 = workbook.add_chart({'type': 'line'})
        chart3.add_series({
            'name':       '=Sheet1!$D$1',
            'categories': '=Sheet1!$A$2:$A$51',
            'values':     '=Sheet1!$D$2:$D$51',
        })
        chart3.set_title ({'name': 'Model Equation vs Probability'})
        chart3.set_x_axis({'name': 'Probability'})
        chart3.set_y_axis({'name': 'Model Equation'})
        chart3.set_style(10)

        # push charts into the worksheet
        worksheet.insert_chart('D2', chart2, {'x_offset': 30, 'y_offset': 10})
        worksheet.insert_chart('D2', chart1, {'x_offset': 30, 'y_offset': 355})
        worksheet.insert_chart('D2', chart3, {'x_offset': 30, 'y_offset': 500})
        workbook.close()

    # simulation_info method
    def simulation_info(self):
        for i in range(len(self.simulationList)):
            print(self.simulationList[i].prob)
            print(self.simulationList[i].avg_forwarded)
            print(self.simulationList[i].avg_dropped)

    # controls simulations
    def simulate(self):
        while self.p <= 1.02:
            total_forwarded = 0.0
            total_dropped = 0.0
            for simulationIndex in range(1, PacketSimulation.NUMBER_OF_SIMULATIONS + 1):
                packet_arrived = 0
                packet_dropped = 0
                for slotIndex in range(1, PacketSimulation.NUMBER_OF_SLOTS + 1):
                    if random.random() < self.p:
                        packet_arrived += 1
                packet_forwarded = packet_arrived
                if packet_arrived > 3:
                    packet_dropped = packet_arrived - 3
                    packet_forwarded = 3
                total_dropped += packet_dropped
                total_forwarded += packet_forwarded
                simulationIndex += 1
            self.simulationList.append(Simulation(self.p, total_dropped / PacketSimulation.NUMBER_OF_SIMULATIONS, total_forwarded / PacketSimulation.NUMBER_OF_SIMULATIONS))
            self.modelEquationList.append(Model(self.p,  3 - (3 * math.pow(1-self.p, 10)) - (20 * self.p * (math.pow(1 - self.p, 9))) - (45 * math.pow(self.p, 2) * math.pow(1-self.p, 8))))
            self.p += PacketSimulation.PROBABILITY_INCREMENT


def main():
    p = PacketSimulation()
    p.simulate()
    p.create_excel()


if __name__ == '__main__':
    main()
